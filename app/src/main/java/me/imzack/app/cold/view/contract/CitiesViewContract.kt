package me.imzack.app.cold.view.contract

import me.imzack.app.cold.view.adapter.CityAdapter

interface CitiesViewContract : BaseViewContract {

    fun showInitialView(cityAdapter: CityAdapter)

    fun onDetectedNetworkNotAvailable()

    fun showCityDeletionAlertDialog(cityName: String, position: Int)
}
