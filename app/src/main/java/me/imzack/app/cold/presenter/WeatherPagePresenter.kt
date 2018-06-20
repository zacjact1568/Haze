package me.imzack.app.cold.presenter

import me.imzack.app.cold.App
import me.imzack.app.cold.R
import me.imzack.app.cold.common.Constant
import me.imzack.app.cold.event.CityDeletedEvent
import me.imzack.app.cold.event.WeatherUpdateStatusChangedEvent
import me.imzack.app.cold.model.DataManager
import me.imzack.app.cold.model.bean.FormattedWeather
import me.imzack.app.cold.model.bean.Weather
import me.imzack.app.cold.util.*
import me.imzack.app.cold.view.contract.WeatherPageViewContract
import org.greenrobot.eventbus.Subscribe

class WeatherPagePresenter(private var weatherPageViewContract: WeatherPageViewContract?, private var weatherListPosition: Int) : BasePresenter() {

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
        if (weather.isNewAdded && !weather.isUpdating) {
            notifyUpdatingWeather()
            updateWeatherUpdateStatus()
        }
        // TODO 在这里检查上一次更新的时间，若相隔过长，也自动更新
    }

    fun notifyUpdatingWeather() {
        DataManager.updateWeatherDataFromInternet(weather.cityId)
    }

    private val formattedWeather: FormattedWeather
        get() {
            return if (weather.isNewAdded) {
                FormattedWeather(weather.isUpdating, weather.cityName)
            } else {
                FormattedWeather(
                        weather.isUpdating,
                        weather.cityName,
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

    /** 判断是否是当前城市 */
    private fun isThisCity(cityId: String) = weather.cityId == cityId || DataManager.isLocationCity(weatherListPosition)

    @Subscribe
    fun onWeatherUpdateStatusChanged(event: WeatherUpdateStatusChangedEvent) {
        // 如果更新事件不是针对当前城市的，返回
        if (!isThisCity(event.cityId)) return
        when (event.status) {
            WeatherUpdateStatusChangedEvent.STATUS_UPDATING, WeatherUpdateStatusChangedEvent.STATUS_FAILED -> updateWeatherUpdateStatus()
            WeatherUpdateStatusChangedEvent.STATUS_LOCATED -> weatherPageViewContract!!.changeCityName(weather.cityName)
            WeatherUpdateStatusChangedEvent.STATUS_UPDATED -> weatherPageViewContract!!.showWeatherView(formattedWeather)
        }
    }

    // 将优先级设为 1（默认为 0），否则将优先通知 WeatherPresenter 中的订阅者（可能是因为先注册），将先调用 notifyDataSetChanged，达不到更新 ViewPager 的效果
    @Subscribe(priority = 1)
    fun onCityDeleted(event: CityDeletedEvent) {
        if (isThisCity(event.cityId)) {
            // Same as "event.position == weatherListPosition"
            // 如果删除的是当前城市
            weatherPageViewContract!!.onCityDeleted()
        } else if (event.position < weatherListPosition) {
            // 如果删除的城市的位置在当前城市之前
            // 此城市在 weather list 中的位置前进了一位，因此需要将 weatherListPosition 减 1
            weatherPageViewContract!!.onPositionChanged(--weatherListPosition)
        }
        // 如果删除的城市的位置在当前城市之后，不做任何操作
    }
}
