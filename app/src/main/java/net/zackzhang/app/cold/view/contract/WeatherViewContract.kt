package net.zackzhang.app.cold.view.contract

import net.zackzhang.app.cold.view.adapter.WeatherPagerAdapter

interface WeatherViewContract : BaseViewContract {

    fun showInitialView(weatherPagerAdapter: WeatherPagerAdapter, isCityEmpty: Boolean)

    fun onDetectedNetworkNotAvailable()

    fun onSwitchPage(position: Int)

    fun onPageEmptyStateChanged(isEmpty: Boolean)
}