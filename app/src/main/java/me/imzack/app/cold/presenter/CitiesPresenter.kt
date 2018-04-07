package me.imzack.app.cold.presenter

import me.imzack.app.cold.App
import me.imzack.app.cold.event.CityAddedEvent
import me.imzack.app.cold.event.CityDeletedEvent
import me.imzack.app.cold.event.CitySelectedEvent
import me.imzack.app.cold.event.WeatherUpdateStatusChangedEvent
import me.imzack.app.cold.model.DataManager
import me.imzack.app.cold.util.SystemUtil
import me.imzack.app.cold.view.adapter.CityAdapter
import me.imzack.app.cold.view.contract.CitiesViewContract
import org.greenrobot.eventbus.Subscribe

class CitiesPresenter(private var citiesViewContract: CitiesViewContract?) : BasePresenter() {
    
    private val cityAdapter = CityAdapter()
    private val eventBus = App.eventBus

    override fun attach() {
        eventBus.register(this)
        cityAdapter.onCityItemClickListener = {
            eventBus.post(CitySelectedEvent(presenterName, DataManager.getWeather(it).cityId, it))
        }
        cityAdapter.onUpdateButtonClickListener = {
            if (!DataManager.isWeatherDataOnUpdating(it)) {
                // 说明现在未在更新，响应请求
                updateWeather(it)
            }
        }
        cityAdapter.onDeleteButtonClickListener = {
            citiesViewContract!!.showCityDeletionConfirmationDialog(DataManager.getWeather(it).cityName, it)
        }
        citiesViewContract!!.showInitialView(cityAdapter)
    }

    override fun detach() {
        citiesViewContract = null
        eventBus.unregister(this)
    }

    fun notifyCityDeleted(position: Int) {
        // 需要在执行删除前获取到 cityId
        val cityId = DataManager.getWeather(position).cityId
        // 执行删除
        DataManager.notifyCityDeleted(position)
        cityAdapter.notifyItemRemoved(position)
        eventBus.post(CityDeletedEvent(presenterName, cityId, position))
    }

    private fun updateWeather(position: Int) {
        if (SystemUtil.isNetworkAvailable) {
            // 开始更新数据
            DataManager.getWeatherDataFromInternet(DataManager.getWeather(position).cityId)
            // 刷新适配器（显示出正在更新的状态）
            cityAdapter.notifyItemChanged(position)
        } else {
            // 显示网络不可用的 SnackBar
            citiesViewContract!!.onDetectedNetworkNotAvailable()
        }
    }

    @Subscribe
    fun onCityAdded(event: CityAddedEvent) {
        cityAdapter.notifyItemInserted(DataManager.recentlyAddedCityLocation)
    }

    @Subscribe
    fun onWeatherUpdateStatusChanged(event: WeatherUpdateStatusChangedEvent) {
        // 刷新适配器：
        // 1. 正在更新，表现为添加正在更新的状态（旋转箭头）
        // 2. 成功更新，表现为刷新数据且取消正在更新的状态
        // 3. 更新失败，表现为仅取消正在更新的状态
        cityAdapter.notifyItemChanged(event.position)
    }
}
