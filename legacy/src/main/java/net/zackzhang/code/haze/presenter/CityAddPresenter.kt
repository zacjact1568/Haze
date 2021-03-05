package net.zackzhang.code.haze.presenter

import android.text.TextUtils
import net.zackzhang.code.haze.App
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.event.CityAddedEvent
import net.zackzhang.code.haze.model.DataManager
import net.zackzhang.code.haze.model.bean.City
import net.zackzhang.code.haze.model.bean.Weather
import net.zackzhang.code.haze.view.adapter.CitySearchResultAdapter
import net.zackzhang.code.haze.view.contract.CityAddViewContract

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
