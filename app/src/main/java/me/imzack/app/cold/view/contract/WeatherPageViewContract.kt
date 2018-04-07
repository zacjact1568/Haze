package me.imzack.app.cold.view.contract

import me.imzack.app.cold.model.bean.FormattedWeather

interface WeatherPageViewContract : BaseViewContract {

    fun showInitialView(formattedWeather: FormattedWeather)

    fun onDetectedNetworkNotAvailable()

    fun onWeatherUpdatedSuccessfully(formattedWeather: FormattedWeather)

    fun onWeatherUpdatedAbortively()

    fun onChangeSwipeRefreshingStatus(isRefreshing: Boolean)

    fun onCityDeleted()

    fun onPositionChanged(newPosition: Int)
}
