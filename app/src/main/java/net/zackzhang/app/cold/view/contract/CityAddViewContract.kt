package net.zackzhang.app.cold.view.contract

import net.zackzhang.app.cold.view.adapter.CitySearchResultAdapter

interface CityAddViewContract : BaseViewContract {

    fun showInitialView(citySearchResultAdapter: CitySearchResultAdapter)

    fun onSearchTextEmptied()
}
