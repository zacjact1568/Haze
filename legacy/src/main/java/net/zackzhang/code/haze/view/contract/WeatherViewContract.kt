package net.zackzhang.code.haze.view.contract

import net.zackzhang.code.haze.view.adapter.WeatherPagerAdapter

interface WeatherViewContract : BaseViewContract {

    fun showInitialView(weatherPagerAdapter: WeatherPagerAdapter, isCityEmpty: Boolean)

    fun onDetectedNetworkNotAvailable()

    fun onSwitchPage(position: Int)

    fun onCityEmptyStateChanged(isEmpty: Boolean)
}