package net.zackzhang.app.cold.view.contract

import net.zackzhang.app.cold.view.adapter.CityAdapter

interface CitiesViewContract : BaseViewContract {

    fun showInitialView(cityAdapter: CityAdapter)

    fun showCityDeletionConfirmationDialog(cityName: String, position: Int)
}
