package net.zackzhang.app.cold.presenter

import android.text.TextUtils
import net.zackzhang.app.cold.App
import net.zackzhang.app.cold.R
import net.zackzhang.app.cold.event.CityAddedEvent
import net.zackzhang.app.cold.model.DataManager
import net.zackzhang.app.cold.model.bean.City
import net.zackzhang.app.cold.model.bean.Weather
import net.zackzhang.app.cold.view.adapter.CitySearchResultAdapter
import net.zackzhang.app.cold.view.contract.CityAddViewContract

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
            DataManager.notifyAddingCity(Weather(id, name, name == prefecture))
            App.eventBus.post(CityAddedEvent(
                    javaClass.simpleName,
                    id,
                    DataManager.cityCount
            ))
            cityAddViewContract!!.exit()
        }
    }
}
