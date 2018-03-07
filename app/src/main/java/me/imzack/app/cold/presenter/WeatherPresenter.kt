package me.imzack.app.cold.presenter

import android.support.v4.app.FragmentManager
import me.imzack.app.cold.App
import me.imzack.app.cold.R
import me.imzack.app.cold.event.*
import me.imzack.app.cold.model.DataManager
import me.imzack.app.cold.util.SystemUtil
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

        if (SystemUtil.isNetworkAvailable) {
            // 如果网络可用，发起网络访问
            DataManager.getWeatherDataFromInternet(DataManager.getWeather(DataManager.recentlyAddedCityLocation).cityId)
        } else {
            // 如果网络不可用，用 SnackBar 提示
            weatherViewContract!!.onDetectedNetworkNotAvailable()
        }
    }

    @Subscribe
    fun onCityDeleted(event: CityDeletedEvent) {
        weatherPagerAdapter.notifyDataSetChanged()
        updatePageEmptyState()
    }

    @Subscribe
    fun onWeatherUpdateStatusChanged(event: WeatherUpdateStatusChangedEvent) {
        //这里，处理MyCitiesPresenter和WeatherPresenter的onWeatherUpdated中可能出现冲突或重复的语句
        when (event.status) {
            WeatherUpdateStatusChangedEvent.STATUS_ON_UPDATING -> { }
            //显示toast，提示更新成功或更新失败
            WeatherUpdateStatusChangedEvent.STATUS_UPDATED_SUCCESSFUL -> weatherViewContract!!.showToast(R.string.toast_weather_update_successfully)
            WeatherUpdateStatusChangedEvent.STATUS_UPDATED_FAILED -> weatherViewContract!!.showToast(R.string.toast_weather_update_failed)
        }
        //TODO 可以在这里处理开始更新的事件
    }

    @Subscribe
    fun onCitySelected(event: CitySelectedEvent) {
        weatherViewContract!!.onSwitchPage(event.position)
    }
}