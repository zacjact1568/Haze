package net.zackzhang.code.haze.view.contract

import net.zackzhang.code.haze.view.adapter.CitySearchResultAdapter

interface CityAddViewContract : BaseViewContract {

    fun showInitialView(citySearchResultAdapter: CitySearchResultAdapter)

    fun onSearchTextEmptied()
}
