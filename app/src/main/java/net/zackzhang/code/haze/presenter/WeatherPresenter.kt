package net.zackzhang.code.haze.presenter

import android.support.v4.app.FragmentManager
import net.zackzhang.code.haze.App
import net.zackzhang.code.haze.event.*
import net.zackzhang.code.haze.model.DataManager
import net.zackzhang.code.haze.view.adapter.WeatherPagerAdapter
import net.zackzhang.code.haze.view.contract.WeatherViewContract
import org.greenrobot.eventbus.Subscribe

class WeatherPresenter(private var weatherViewContract: WeatherViewContract?, fragmentManager: FragmentManager) : BasePresenter() {

    private val eventBus = App.eventBus
    private val weatherPagerAdapter = WeatherPagerAdapter(fragmentManager)

    override fun attach() {
        eventBus.register(this)
        // 由于在 WeatherFragment 中初始化该类后会马上调用该函数，因此 isCityEmpty 就是真实值
        weatherViewContract!!.showInitialView(weatherPagerAdapter, DataManager.cityCount == 0)
    }

    override fun detach() {
        weatherViewContract = null
        eventBus.unregister(this)
    }

    @Subscribe
    fun onDataLoaded(event: DataLoadedEvent) {
        weatherPagerAdapter.notifyDataSetChanged()
    }

    @Subscribe
    fun onCityAdded(event: CityAddedEvent) {
        // 刷新 pager，先将无天气数据的城市放到界面上
        weatherPagerAdapter.notifyDataSetChanged()
    }

    @Subscribe
    fun onCityDeleted(event: CityDeletedEvent) {
        weatherPagerAdapter.notifyDataSetChanged()
    }

    @Subscribe
    fun onCitySelected(event: CitySelectedEvent) {
        weatherViewContract!!.onSwitchPage(event.position)
    }

    @Subscribe
    fun onCityEmptyStateChanged(event: CityEmptyStateChangedEvent) {
        weatherViewContract!!.onCityEmptyStateChanged(event.isEmpty)
    }
}