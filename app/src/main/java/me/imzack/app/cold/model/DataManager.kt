package me.imzack.app.cold.model

import me.imzack.app.cold.App
import me.imzack.app.cold.event.DataLoadedEvent
import me.imzack.app.cold.event.WeatherUpdateStatusChangedEvent
import me.imzack.app.cold.model.bean.Weather
import me.imzack.app.cold.model.database.DatabaseHelper
import me.imzack.app.cold.model.network.NetworkHelper
import me.imzack.app.cold.model.preference.PreferenceHelper
import me.imzack.app.cold.util.WeatherUtil

object DataManager {

    val databaseHelper = DatabaseHelper()
    val networkHelper = NetworkHelper()
    val preferenceHelper = PreferenceHelper()

    private val weatherList = mutableListOf<Weather>()

    var isDataLoaded = false
        private set

    fun loadData() {
        if (isDataLoaded) return
        databaseHelper.loadWeatherAsync {
            weatherList.addAll(it)
            isDataLoaded = true
            App.eventBus.post(DataLoadedEvent())
        }
    }

    fun unloadData() {
        if (!isDataLoaded) return
        isDataLoaded = false
        weatherList.clear()
    }

    // **************** WeatherList ****************

    fun getWeather(location: Int) = weatherList[location]

    val cityCount
        get() = weatherList.size

    val recentlyAddedCityLocation
        get() = cityCount - 1

    /** 根据 cityId 找其在 weatherList 中的位置，若未找到，返回 -1 */
    fun getWeatherLocationInWeatherList(cityId: String) = (0 until cityCount).firstOrNull { getWeather(it).cityId == cityId } ?: -1

    fun notifyCityAdded(cityId: String, cityName: String) {
        val weather = Weather(cityId, cityName)
        // 产生空 weather 对象，添加到 weatherList
        weatherList.add(weather)
        // 存储数据到数据库
        databaseHelper.insertWeather(weather)
    }

    fun notifyCityDeleted(location: Int) {
        val weather = getWeather(location)
        // 将天气数据标记为已删除
        weather.status = Weather.STATUS_DELETED
        weatherList.removeAt(location)
        databaseHelper.deleteWeather(weather.cityId)
    }

    /** 天气数据是否正在更新 */
    fun isWeatherDataOnUpdating(location: Int) = getWeather(location).isOnUpdating

    /** 检测城市是否已存在 */
    fun doesCityExist(cityId: String) = weatherList.any { it.cityId == cityId }

    /** 发起网络访问，获取天气数据 */
    fun getWeatherDataFromInternet(cityId: String) {
        val location = getWeatherLocationInWeatherList(cityId)
        val eventSource = javaClass.simpleName
        // 标记为正在刷新
        getWeather(location).status = Weather.STATUS_ON_UPDATING
        // 发送开始更新的事件，通知 presenter 更新
        App.eventBus.post(WeatherUpdateStatusChangedEvent(
                eventSource,
                cityId,
                location,
                WeatherUpdateStatusChangedEvent.STATUS_ON_UPDATING
        ))
        // 异步发起网络访问
        networkHelper.getHeWeatherDataAsync(cityId) {
            // 取消刷新状态
            getWeather(location).status = Weather.STATUS_GENERAL
            // 是否成功获取到所有数据
            val successful = it.common != null && it.air != null
            if (successful) {
                val weather = getWeather(location)
                // 解析返回的天气数据为 weather 对象的格式
                WeatherUtil.parseHeWeather(it, weather)
                // 更新数据库中储存的天气
                databaseHelper.updateWeather(weather)
            }
            // 发送更新完成的事件，通知 presenter 更新
            App.eventBus.post(WeatherUpdateStatusChangedEvent(
                    eventSource,
                    cityId,
                    location,
                    if (successful) WeatherUpdateStatusChangedEvent.STATUS_UPDATED_SUCCESSFUL else WeatherUpdateStatusChangedEvent.STATUS_UPDATED_FAILED
            ))
        }
    }

    // **************** Condition ****************

    fun getConditionByCode(code: Int) = databaseHelper.queryConditionByCode(code) ?: throw IllegalArgumentException("No condition corresponds to code \"$code\"")
}
