package net.zackzhang.code.haze.view.contract

import net.zackzhang.code.haze.view.adapter.CityAdapter

interface CitiesViewContract : BaseViewContract {

    fun showInitialView(cityAdapter: CityAdapter, isCityEmpty: Boolean)

    fun showCityDeletionConfirmationDialog(cityName: String, position: Int)

    fun onCityEmptyStateChanged(isEmpty: Boolean)
}
