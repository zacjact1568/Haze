package net.zackzhang.app.cold.view.contract

import net.zackzhang.app.cold.model.bean.FormattedWeather

interface WeatherPageViewContract : BaseViewContract {

    fun showInitialView(formattedWeather: FormattedWeather)

    fun showWeatherView(formattedWeather: FormattedWeather)

    fun changeSwipeRefreshingStatus(isRefreshing: Boolean)

    fun changeCityName(cityName: String)
}
