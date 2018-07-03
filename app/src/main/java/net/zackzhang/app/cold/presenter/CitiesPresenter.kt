package net.zackzhang.app.cold.presenter

import net.zackzhang.app.cold.App
import net.zackzhang.app.cold.event.CityAddedEvent
import net.zackzhang.app.cold.event.CityDeletedEvent
import net.zackzhang.app.cold.event.CitySelectedEvent
import net.zackzhang.app.cold.event.WeatherUpdateStatusChangedEvent
import net.zackzhang.app.cold.model.DataManager
import net.zackzhang.app.cold.view.adapter.CityAdapter
import net.zackzhang.app.cold.view.contract.CitiesViewContract
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
        eventBus.post(CityDeletedEvent(javaClass.simpleName, cityId, position))
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
