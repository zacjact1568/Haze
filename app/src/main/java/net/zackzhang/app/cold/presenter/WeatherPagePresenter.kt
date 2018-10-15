package net.zackzhang.app.cold.presenter

import net.zackzhang.app.cold.App
import net.zackzhang.app.cold.R
import net.zackzhang.app.cold.common.Constant
import net.zackzhang.app.cold.event.WeatherUpdateStatusChangedEvent
import net.zackzhang.app.cold.model.DataManager
import net.zackzhang.app.cold.model.bean.FormattedWeather
import net.zackzhang.app.cold.model.bean.Weather
import net.zackzhang.app.cold.util.*
import net.zackzhang.app.cold.view.contract.WeatherPageViewContract
import org.greenrobot.eventbus.Subscribe

class WeatherPagePresenter(private var weatherPageViewContract: WeatherPageViewContract?, private val weatherListPosition: Int) : BasePresenter() {

    private val weather = DataManager.getWeather(weatherListPosition)

    private val eventBus = App.eventBus

    override fun attach() {
        eventBus.register(this)
        weatherPageViewContract!!.showInitialView(formattedWeather)
    }

    override fun detach() {
        weatherPageViewContract = null
        eventBus.unregister(this)
    }

    fun notifyStartingUpCompleted() {
        // 启动该 Fragment 后，检查该城市是否是刚添加且未被标记为正在更新的，如果是，自动更新天气
//        if (weather.isNewAdded && !weather.isUpdating) {
//            notifyUpdatingWeather()
//            updateWeatherUpdateStatus()
//        }
        // TODO 在这里检查上一次更新的时间，若相隔过长，也自动更新
    }

    fun notifyUpdatingWeather() {
        DataManager.updateWeatherDataFromInternet(weather.cityId)
    }

    private val formattedWeather: FormattedWeather
        get() {
            val isLocationCity = DataManager.isLocationCity(weatherListPosition)
            return if (weather.isNewAdded) {
                FormattedWeather(weather.isUpdating, weather.cityName, isLocationCity)
            } else {
                FormattedWeather(
                        weather.isUpdating,
                        weather.cityName,
                        isLocationCity,
                        DataManager.getConditionByCode(weather.current.conditionCode),
                        weather.current.temperature.toString(),
                        String.format(ResourceUtil.getString(R.string.text_update_time), TimeUtil.formatTime(weather.updateTime)),
                        weather.current.feelsLike.toString(),
                        // 今天的最高温和最低温
                        "${weather.dailyForecasts[0].temperatureMin} | ${weather.dailyForecasts[0].temperatureMax}",
                        if (weather.current.airQualityIndex == 0) Constant.UNKNOWN_DATA else HeWeatherUtil.parseAqi(weather.current.airQualityIndex),
                        TimeUtil.getWeeks(weather.dailyForecasts[0].date, Weather.DAILY_FORECAST_LENGTH_DISPLAY),
                        Array(Weather.DAILY_FORECAST_LENGTH_DISPLAY) { DataManager.getConditionByCode(weather.dailyForecasts[it].conditionCodeDay) },
                        IntArray(Weather.DAILY_FORECAST_LENGTH_DISPLAY) { weather.dailyForecasts[it].temperatureMax },
                        IntArray(Weather.DAILY_FORECAST_LENGTH_DISPLAY) { weather.dailyForecasts[it].temperatureMin }
                )
            }
        }

    /** 更新界面上的更新状态 */
    private fun updateWeatherUpdateStatus() {
        weatherPageViewContract!!.changeSwipeRefreshingStatus(weather.isUpdating)
    }

    @Subscribe
    fun onWeatherUpdateStatusChanged(event: WeatherUpdateStatusChangedEvent) {
        // 如果更新事件不是针对当前城市的，返回
        if (weather.cityId != event.cityId && !DataManager.isLocationCity(event.position)) return
        when (event.status) {
            WeatherUpdateStatusChangedEvent.STATUS_UPDATING, WeatherUpdateStatusChangedEvent.STATUS_FAILED -> updateWeatherUpdateStatus()
            WeatherUpdateStatusChangedEvent.STATUS_LOCATED -> weatherPageViewContract!!.changeCityName(weather.cityName)
            WeatherUpdateStatusChangedEvent.STATUS_UPDATED -> weatherPageViewContract!!.showWeatherView(formattedWeather)
        }
    }
}
