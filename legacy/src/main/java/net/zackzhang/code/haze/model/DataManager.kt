package net.zackzhang.code.haze.model

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.zackzhang.code.haze.App
import net.zackzhang.code.haze.event.DataLoadedEvent
import net.zackzhang.code.haze.event.WeatherUpdateStatusChangedEvent
import net.zackzhang.code.haze.exception.NetworkNotAvailableException
import net.zackzhang.code.haze.model.bean.Weather
import net.zackzhang.code.haze.model.database.DatabaseHelper
import net.zackzhang.code.haze.model.location.LocationHelper
import net.zackzhang.code.haze.model.network.NetworkHelper
import net.zackzhang.code.haze.model.preference.PreferenceHelper
import net.zackzhang.code.haze.util.HeWeatherUtil
import net.zackzhang.code.haze.util.LogUtil
import net.zackzhang.code.haze.util.SystemUtil

object DataManager {

    val databaseHelper by lazy { DatabaseHelper() }
    val locationHelper by lazy { LocationHelper() }
    val networkHelper by lazy { NetworkHelper() }
    val preferenceHelper by lazy { PreferenceHelper() }

    private val weatherList = mutableListOf<Weather>()

    private val eventBus = App.eventBus
    
    var isDataLoaded = false
        private set

    private var weatherUpdateDisposable: Disposable? = null

    fun loadData() {
        if (isDataLoaded) return
        databaseHelper.loadWeatherAsync {
            weatherList.addAll(it)
            isDataLoaded = true
            eventBus.post(DataLoadedEvent())
        }
    }

    fun unloadData() {
        if (!isDataLoaded) return
        isDataLoaded = false
        weatherList.clear()
    }

    /** 退出 app（HomeActivity.onDestroy）时调用该函数，停止当前进行的任务，保证后台运行时无任务 */
    fun stopAllTasks() {
        // 先不中断，因为只要 app 在后台，天气更新就不应该被打断
        if (weatherUpdateDisposable != null && !weatherUpdateDisposable!!.isDisposed) {
            // 如果天气正在更新，取消
            weatherUpdateDisposable!!.dispose()
        }
    }

    // **************** WeatherList ****************

    fun getWeather(location: Int) = weatherList[location]

    val cityCount
        get() = weatherList.size

    fun getWeatherLocationInWeatherList(cityId: String) = (0 until cityCount).firstOrNull { getWeather(it).cityId == cityId } ?: throw IllegalArgumentException("No weather item matches city id \"$cityId\"")

    // 如果未指定 location，而且 weather 中 addTime 为 0，说明是定位城市，默认插入到表头，否则插入到表尾
    // 在删除城市后恢复需要自行在删除前记录 location，在这里指定 TODO 待实现
    fun notifyAddingCity(weather: Weather, location: Int = if (weather.addTime == 0L) 0 else cityCount) {
        weatherList.add(location, weather)
        databaseHelper.insertWeather(weather)
    }

    fun notifyDeletingCity(location: Int) {
        val weather = getWeather(location)
        // 将天气数据标记为已删除
        weather.status = Weather.STATUS_DELETED
        weatherList.removeAt(location)
        databaseHelper.deleteWeather(weather.cityId)
    }

    /** 天气数据是否正在更新 */
    fun isWeatherDataOnUpdating(location: Int) = getWeather(location).isUpdating

    /** 检测城市是否已存在 */
    fun doesCityExist(cityId: String) = weatherList.any { it.cityId == cityId }

    /** 判断是否是定位城市 */
    fun isLocationCity(location: Int) = preferenceHelper.locationServiceValue && location == 0

    /**
     * 发起网络访问，更新天气数据，更新结果通过 EventBus 通知所有订阅了 WeatherUpdateStatusChangedEvent 事件的组件
     * @return 用来中断操作的 Disposable 对象
     */
    fun updateWeatherDataFromInternet(cityId: String) {
        val className = javaClass.simpleName
        // 获取城市在 weatherList 中的位置
        // 如果还未定位，cityId 为 current_location，返回 0
        val weatherListLocation = getWeatherLocationInWeatherList(cityId)
        // 事先获取 weather 的引用
        // 如果后续在异步操作的回调函数中通过 weatherListLocation 去取，可能取到的已经不是这个 weather 了
        // 因为可能已经添加或删除过城市了
        val weather = getWeather(weatherListLocation)
        val cityIdSingle = if (isLocationCity(weatherListLocation)) {
            // 如果开启了位置服务，并且要更新的城市是第一个，说明这个城市是定位城市，在刷新天气之前还要刷新位置
            // 高德定位获取经纬度
            // 不用切换至 IO 线程，因为高德定位自带专门的线程
            locationHelper.getAMapLocationData()
                    .doOnSuccess {
                        // 高德定位服务定位成功
                        LogUtil.d("AMap Location Service: $it")
                    }
                    // flatMap 回调发生在观察者处，线程由 observeOn 指定
                    // 因此，若不切换至 IO 线程，就会在主线程中进行网络请求，会报错
                    .observeOn(Schedulers.io())
                    // 和风天气查询城市 id
                    .flatMap { networkHelper.getHeWeatherCityData(it.first, it.second) }
                    .observeOn(AndroidSchedulers.mainThread())
                    // 解析返回的城市数据，更新 weather.city
                    .map {
                        HeWeatherUtil.parseCity(it, weather.city)
                        weather.cityId
                    }
                    .doOnSuccess {
                        // 和风天气按经纬度查询城市信息成功
                        LogUtil.d("HeWeather City: ${weather.cityName}")
                        // 更新数据库中的城市信息
                        databaseHelper.updateWeather(weather)
                        // 恢复为未刷新状态
                        weather.status = Weather.STATUS_GENERAL
                        // 发送定位成功的事件，显示出定位成功的状态，刷新界面上的城市信息
                        eventBus.post(WeatherUpdateStatusChangedEvent(
                                className,
                                it,
                                weatherListLocation,
                                WeatherUpdateStatusChangedEvent.STATUS_LOCATED
                        ))
                    }
        } else {
            // 如果没有开启位置服务，或者开启了，但要更新的城市不是第一个，说明这个城市是用户手动添加的城市，直接根据城市 id 刷新天气
            Single.just(cityId)
        }
        weatherUpdateDisposable = if (weather.isPrefecture) {
            // 如果要更新天气的城市是地级市，更新天气和空气质量
            cityIdSingle
                    .observeOn(Schedulers.io())
                    // 和风天气查询天气和空气质量信息
                    .flatMap { networkHelper.getHeWeatherAndAirData(it) }
                    .observeOn(AndroidSchedulers.mainThread())
                    // 解析返回的天气和空气质量数据，更新 weather 对象
                    .map {
                        HeWeatherUtil.parseWeatherAndAirData(it.first, it.second, weather)
                        weather.cityId
                    }
        } else {
            // 没有访问县级城市空气质量数据的权限
            // 如果要更新天气的城市是县级城市，只更新天气，可以节约访问次数
            cityIdSingle
                    .observeOn(Schedulers.io())
                    .flatMap { networkHelper.getHeWeatherData(it) }
                    .observeOn(AndroidSchedulers.mainThread())
                    // 解析返回的天气数据，更新 weather 对象
                    .map {
                        HeWeatherUtil.parseWeatherData(it, weather)
                        weather.cityId
                    }
        }
                // subscribeOn 指定订阅发生（调用 subscribe）时的线程
                // 此处即指的是 Single.just 的线程
                // 但是此处 Single.just 并不需要在 IO 线程
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    // 刚订阅的时候调用，做一些初始化工作
                    // 判断网络是否可用
                    if (SystemUtil.isNetworkAvailable) {
                        // 将天气标记为正在刷新
                        weather.status = Weather.STATUS_UPDATING
                        // 发送开始更新的事件，通知更新界面，显示出正在刷新的状态
                        eventBus.post(WeatherUpdateStatusChangedEvent(
                                className,
                                cityId,
                                weatherListLocation,
                                WeatherUpdateStatusChangedEvent.STATUS_UPDATING
                        ))
                    } else {
                        // 网络不可用，抛出异常
                        throw NetworkNotAvailableException()
                    }
                }
                .doOnDispose {
                    // 中途取消
                    LogUtil.d("Dispose: ${weather.cityName}")
                    // 恢复为未刷新状态
                    weather.status = Weather.STATUS_GENERAL
                    // 发送更新取消的事件，主要用来取消某些界面上的刷新状态
                    eventBus.post(WeatherUpdateStatusChangedEvent(
                            className,
                            cityId,
                            weatherListLocation,
                            WeatherUpdateStatusChangedEvent.STATUS_CANCELED
                    ))
                }
                .subscribe({
                    // 所有流程执行成功，做收尾工作
                    LogUtil.d("HeWeather Weather (& Air): ${weather.cityName}")
                    // 更新数据库中的天气信息
                    databaseHelper.updateWeather(weather)
                    // 恢复为未刷新状态
                    weather.status = Weather.STATUS_GENERAL
                    // 发送刷新天气完成的事件，刷新界面上的天气信息
                    eventBus.post(WeatherUpdateStatusChangedEvent(
                            className,
                            it,
                            weatherListLocation,
                            WeatherUpdateStatusChangedEvent.STATUS_UPDATED
                    ))
                }, {
                    // 只要中途出错，就会跳过后续所有操作，直接调用这个
                    // 恢复为未刷新状态
                    weather.status = Weather.STATUS_GENERAL
                    // 发送更新失败的事件，主要用来取消某些界面上的刷新状态
                    eventBus.post(WeatherUpdateStatusChangedEvent(
                            className,
                            cityId,
                            weatherListLocation,
                            WeatherUpdateStatusChangedEvent.STATUS_FAILED,
                            it
                    ))
                })
    }

    // **************** Condition ****************

    fun getConditionByCode(code: Int) = databaseHelper.queryConditionByCode(code)
}
