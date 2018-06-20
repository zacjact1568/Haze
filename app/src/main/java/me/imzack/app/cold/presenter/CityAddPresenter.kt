package me.imzack.app.cold.presenter

import android.text.TextUtils
import me.imzack.app.cold.App
import me.imzack.app.cold.R
import me.imzack.app.cold.event.CityAddedEvent
import me.imzack.app.cold.model.DataManager
import me.imzack.app.cold.model.bean.City
import me.imzack.app.cold.model.bean.Weather
import me.imzack.app.cold.view.adapter.CitySearchResultAdapter
import me.imzack.app.cold.view.contract.CityAddViewContract

class CityAddPresenter(private var cityAddViewContract: CityAddViewContract?) : BasePresenter() {

    private val citySearchList = mutableListOf<City>()
    private val citySearchResultAdapter = CitySearchResultAdapter(citySearchList)

    override fun attach() {
        cityAddViewContract!!.showInitialView(citySearchResultAdapter)
    }

    override fun detach() {
        cityAddViewContract = null
    }

    fun notifySearchTextChanged(input: String) {
        //先清除cityList中的内容
        citySearchList.clear()
        if (!TextUtils.isEmpty(input)) {
            //若查询关键词不为空才执行查询
            DataManager.databaseHelper.queryCityLike(input, citySearchList)
            //刷新适配器
            citySearchResultAdapter.notifyDataSetChanged()
        } else {
            //需要先刷新适配器（清空列表上显示的内容）
            citySearchResultAdapter.notifyDataSetChanged()
            cityAddViewContract!!.onSearchTextEmptied()
        }
    }

    fun notifyCityListItemClicked(position: Int) {
        val (id, name, prefecture) = citySearchList[position]
        if (DataManager.doesCityExist(id)) {
            cityAddViewContract!!.showToast(R.string.toast_city_exists)
        } else {
            DataManager.notifyCityAdded(Weather.City(id, name, name == prefecture))
            App.eventBus.post(CityAddedEvent(
                    javaClass.simpleName,
                    id,
                    DataManager.recentlyAddedCityLocation
            ))
            cityAddViewContract!!.exit()
        }
    }
}
