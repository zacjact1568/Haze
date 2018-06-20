package me.imzack.app.cold.view.contract

import me.imzack.app.cold.model.bean.FormattedWeather

interface WeatherPageViewContract : BaseViewContract {

    fun showInitialView(formattedWeather: FormattedWeather)

    fun showWeatherView(formattedWeather: FormattedWeather)

    fun changeSwipeRefreshingStatus(isRefreshing: Boolean)

    fun changeCityName(cityName: String)

    fun onCityDeleted()

    fun onPositionChanged(newPosition: Int)
}
