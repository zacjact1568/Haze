package net.zackzhang.code.haze.presenter

import net.zackzhang.code.haze.App
import net.zackzhang.code.haze.event.*
import net.zackzhang.code.haze.model.DataManager
import net.zackzhang.code.haze.view.adapter.CityAdapter
import net.zackzhang.code.haze.view.contract.CitiesViewContract
import org.greenrobot.eventbus.Subscribe

class CitiesPresenter(private var citiesViewContract: CitiesViewContract?) : BasePresenter() {
    
    private val cityAdapter = CityAdapter()

    private val eventBus = App.eventBus

    override fun attach() {
        eventBus.register(this)
        cityAdapter.onCityItemClickListener = {
            eventBus.post(CitySelectedEvent(javaClass.simpleName, DataManager.getWeather(it).cityId, it))
        }
        cityAdapter.onUpdateButtonClickListener = {
            if (!DataManager.isWeatherDataOnUpdating(it)) {
                // 说明现在未在更新，开始更新数据
                DataManager.updateWeatherDataFromInternet(DataManager.getWeather(it).cityId)
                // 刷新适配器，显示出正在更新的状态
                cityAdapter.notifyItemChanged(it)
            }
        }
        cityAdapter.onDeleteButtonClickListener = {
            citiesViewContract!!.showCityDeletionConfirmationDialog(DataManager.getWeather(it).cityName, it)
        }
        citiesViewContract!!.showInitialView(cityAdapter, DataManager.cityCount == 0)
    }

    override fun detach() {
        citiesViewContract = null
        eventBus.unregister(this)
    }

    fun notifyCityDeleted(position: Int) {
        // 需要在执行删除前获取到 cityId
        val cityId = DataManager.getWeather(position).cityId
        // 执行删除
        DataManager.notifyDeletingCity(position)
        cityAdapter.notifyItemRemoved(position)
        eventBus.post(CityDeletedEvent(javaClass.simpleName, cityId, position))
    }

    @Subscribe
    fun onCityAdded(event: CityAddedEvent) {
        cityAdapter.notifyItemInserted(event.position)
    }

    @Subscribe
    fun onCityDeleted(event: CityDeletedEvent) {
        if (event.eventSource == javaClass.simpleName) return
        cityAdapter.notifyItemRemoved(event.position)
    }

    @Subscribe
    fun onCityEmptyStateChanged(event: CityEmptyStateChangedEvent) {
        citiesViewContract!!.onCityEmptyStateChanged(event.isEmpty)
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
