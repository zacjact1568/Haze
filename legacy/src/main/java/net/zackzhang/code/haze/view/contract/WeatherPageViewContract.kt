package net.zackzhang.code.haze.view.contract

import net.zackzhang.code.haze.model.bean.FormattedWeather

interface WeatherPageViewContract : BaseViewContract {

    fun showInitialView(formattedWeather: FormattedWeather)

    fun showWeatherView(formattedWeather: FormattedWeather)

    fun changeSwipeRefreshingStatus(isRefreshing: Boolean)

    fun changeCityName(cityName: String)
}
