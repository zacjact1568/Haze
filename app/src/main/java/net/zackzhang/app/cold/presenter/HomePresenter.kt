package net.zackzhang.app.cold.presenter

import android.os.Bundle
import net.zackzhang.app.cold.App
import net.zackzhang.app.cold.R
import net.zackzhang.app.cold.common.Constant
import net.zackzhang.app.cold.event.*
import net.zackzhang.app.cold.exception.*
import net.zackzhang.app.cold.model.DataManager
import net.zackzhang.app.cold.util.*
import net.zackzhang.app.cold.view.contract.HomeViewContract
import org.greenrobot.eventbus.Subscribe
import retrofit2.HttpException
import java.io.IOException

class HomePresenter(private var homeViewContract: HomeViewContract?) : BasePresenter() {

    private val eventBus = App.eventBus
    private val preferenceHelper = DataManager.preferenceHelper
    private var lastBackKeyPressedTime = 0L
    /** 指示此 activity 是否是重建过的 */
    private var isRestored = false
    // 需要设置默认为 weather 界面，因为正常启动不会更新此变量
    private var currentFragmentTag = Constant.WEATHER
    private var isCityEmpty = DataManager.cityCount == 0

    override fun attach() {
        eventBus.register(this)
        homeViewContract!!.showInitialView(currentFragmentTag, isCityEmpty)
    }

    override fun detach() {
        homeViewContract = null
        eventBus.unregister(this)
    }

    // 只有 activity 重建过才会调用此函数
    fun notifyInstanceStateRestored(restoredFragmentTag: String) {
        isRestored = true
        currentFragmentTag = restoredFragmentTag
    }

    fun notifyStartingUpCompleted() {
        // 初次启动，进入引导界面
        if (preferenceHelper.needGuideValue) {
            homeViewContract!!.startActivity(Constant.GUIDE)
        }
        // 初始化主页的所有 fragment
        homeViewContract!!.showInitialFragment(isRestored, currentFragmentTag, isCityEmpty)
    }

    fun notifySavingInstanceState(outState: Bundle) {
        outState.putString(Constant.CURRENT_FRAGMENT, currentFragmentTag)
    }

    fun notifySwitchingFragment(toTag: String) {
        switchFragment(toTag)
    }

    fun notifyBackPressed(isDrawerOpen: Boolean, isOnRootFragment: Boolean) {
        val currentTime = System.currentTimeMillis()
        when {
            isDrawerOpen -> homeViewContract!!.closeDrawer()
            //不是在根Fragment（可以直接退出的Fragment）上，回到根Fragment
            !isOnRootFragment -> switchFragment(Constant.WEATHER)
            //连续点击间隔在1.5s以内，执行back键操作
            currentTime - lastBackKeyPressedTime < 1500 -> homeViewContract!!.onPressBackKey()
            //否则更新上次点击back键的时间，并显示一个toast
            else -> {
                lastBackKeyPressedTime = currentTime
                homeViewContract!!.showToast(R.string.toast_double_press_exit)
            }
        }
    }

    fun notifyLocationServicePermissionsRequestFinished(granted: Boolean) {
        if (granted) {
            // 直接刷新列表首位城市的天气，这个城市一定是定位城市
            DataManager.updateWeatherDataFromInternet(DataManager.getWeather(0).cityId)
        } else {
            homeViewContract!!.onLocationServicePermissionsDenied()
        }
    }

    fun notifyDisablingLocationService() {
        preferenceHelper.locationServiceValue = false
        val cityId = DataManager.getWeather(0).cityId
        DataManager.notifyDeletingCity(0)
        eventBus.post(CityDeletedEvent(
                javaClass.simpleName,
                cityId,
                0
        ))
    }

    private fun switchFragment(toTag: String) {
        if (currentFragmentTag == toTag) return
        homeViewContract!!.switchFragment(currentFragmentTag, toTag, isCityEmpty)
        currentFragmentTag = toTag
    }

    /** 统一处理城市是否为空的状态变化事件 */
    private fun updateCityEmptyState() {
        val isCityEmptyNow = DataManager.cityCount == 0
        if (isCityEmpty != isCityEmptyNow) {
            isCityEmpty = isCityEmptyNow
            eventBus.post(CityEmptyStateChangedEvent(isCityEmpty))
        }
    }

    @Subscribe
    fun onDataLoaded(event: DataLoadedEvent) {
        updateCityEmptyState()
    }

    @Subscribe
    fun onCityAdded(event: CityAddedEvent) {
        updateCityEmptyState()
    }

    @Subscribe
    fun onCityDeleted(event: CityDeletedEvent) {
        updateCityEmptyState()
    }

    @Subscribe
    fun onCitySelected(event: CitySelectedEvent) {
        switchFragment(Constant.WEATHER)
    }

    @Subscribe
    fun onCityEmptyStateChanged(event: CityEmptyStateChangedEvent) {
        homeViewContract!!.onCityEmptyStateChanged(event.isEmpty, currentFragmentTag)
    }

    // 集中处理 WeatherPageFragment 和 CitiesFragment 中的天气更新状态改变事件
    @Subscribe
    fun onWeatherUpdateStatusChanged(event: WeatherUpdateStatusChangedEvent) {
        when (event.status) {
            // TODO 更新成功的 toast 添加城市名称
            WeatherUpdateStatusChangedEvent.STATUS_UPDATED -> homeViewContract!!.showToast(R.string.toast_weather_update_successfully)
            WeatherUpdateStatusChangedEvent.STATUS_FAILED -> {
                val error = event.error
                when (error) {
                    null -> throw IllegalArgumentException("The status of WeatherUpdateStatusChangedEvent is STATUS_FAILED but error property is null")
                    is NetworkNotAvailableException -> homeViewContract!!.onDetectedNetworkNotAvailable()
                    is SystemLocationServiceDisabledException -> homeViewContract!!.onDetectedSystemLocationServiceDisabled()
                    is NoEnoughPermissionsGrantedException -> homeViewContract!!.onDetectedNoEnoughPermissionsGranted()
                    is AMapLocationServiceException -> homeViewContract!!.showToast(AMapLocationUtil.parseErrorCode(error.code))
                    is IOException -> homeViewContract!!.showToast(R.string.error_io_exception)
                    is HttpException -> homeViewContract!!.showToast(R.string.error_http_exception)
                    is HeWeatherServiceException -> homeViewContract!!.showToast(HeWeatherUtil.parseStatus(error.state))
                    else -> homeViewContract!!.showToast(error.message ?: "null")
                }
                LogUtil.e(error.message!!, error.javaClass.simpleName)
            }
        }
    }
}
