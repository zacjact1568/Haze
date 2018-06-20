package me.imzack.app.cold.presenter

import android.support.v4.app.FragmentManager
import me.imzack.app.cold.App
import me.imzack.app.cold.R
import me.imzack.app.cold.event.*
import me.imzack.app.cold.model.DataManager
import me.imzack.app.cold.view.adapter.WeatherPagerAdapter
import me.imzack.app.cold.view.contract.WeatherViewContract
import org.greenrobot.eventbus.Subscribe

class WeatherPresenter(private var weatherViewContract: WeatherViewContract?, fragmentManager: FragmentManager) : BasePresenter() {

    private val eventBus = App.eventBus
    private val weatherPagerAdapter = WeatherPagerAdapter(fragmentManager)
    private val isCityEmpty
        get() = DataManager.cityCount == 0

    override fun attach() {
        eventBus.register(this)
        weatherViewContract!!.showInitialView(weatherPagerAdapter, isCityEmpty)
    }

    override fun detach() {
        weatherViewContract = null
        eventBus.unregister(this)
    }

    private fun updatePageEmptyState() {
        weatherViewContract!!.onPageEmptyStateChanged(isCityEmpty)
    }

    @Subscribe
    fun onDataLoaded(event: DataLoadedEvent) {
        weatherPagerAdapter.notifyDataSetChanged()
        updatePageEmptyState()
    }

    @Subscribe
    fun onCityAdded(event: CityAddedEvent) {
        // 刷新 pager，先将无天气数据的城市放到界面上
        weatherPagerAdapter.notifyDataSetChanged()
        updatePageEmptyState()
    }

    @Subscribe
    fun onCityDeleted(event: CityDeletedEvent) {
        weatherPagerAdapter.notifyDataSetChanged()
        updatePageEmptyState()
    }

    @Subscribe
    fun onCitySelected(event: CitySelectedEvent) {
        weatherViewContract!!.onSwitchPage(event.position)
    }
}