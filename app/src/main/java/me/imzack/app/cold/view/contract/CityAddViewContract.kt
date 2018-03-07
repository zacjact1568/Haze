package me.imzack.app.cold.view.contract

import me.imzack.app.cold.view.adapter.CitySearchResultAdapter

interface CityAddViewContract : BaseViewContract {

    fun showInitialView(citySearchResultAdapter: CitySearchResultAdapter)

    fun onSearchTextEmptied()
}
