package me.imzack.app.cold.view.contract

import me.imzack.app.cold.view.adapter.WeatherPagerAdapter

interface WeatherViewContract : BaseViewContract {

    fun showInitialView(weatherPagerAdapter: WeatherPagerAdapter, isCityEmpty: Boolean)

    fun onDetectedNetworkNotAvailable()

    fun onSwitchPage(position: Int)

    fun onPageEmptyStateChanged(isEmpty: Boolean)
}