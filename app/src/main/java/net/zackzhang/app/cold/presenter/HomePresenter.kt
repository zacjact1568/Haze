package net.zackzhang.app.cold.presenter

import android.os.Bundle
import net.zackzhang.app.cold.App
import net.zackzhang.app.cold.R
import net.zackzhang.app.cold.common.Constant
import net.zackzhang.app.cold.event.CitySelectedEvent
import net.zackzhang.app.cold.event.WeatherUpdateStatusChangedEvent
import net.zackzhang.app.cold.exception.AMapLocationServiceException
import net.zackzhang.app.cold.exception.HeWeatherServiceException
import net.zackzhang.app.cold.exception.NetworkNotAvailableException
import net.zackzhang.app.cold.exception.SystemLocationServiceDisabledException
import net.zackzhang.app.cold.model.DataManager
import net.zackzhang.app.cold.util.*
import net.zackzhang.app.cold.view.contract.HomeViewContract
import net.zackzhang.app.cold.view.fragment.LocationServicePermissionsFragment
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

    override fun attach() {
        eventBus.register(this)
        homeViewContract!!.showInitialView()
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
        // 如果用户在系统设置中改变了权限，app 会被杀死，而 HomeActivity 又是此 app 的唯一入口，因此在这里检测是否还拥有权限
        if (preferenceHelper.locationServiceValue && !SystemUtil.checkPermissions(LocationServicePermissionsFragment.PERMISSIONS)) {
            // 如果设置中开启了位置服务，而又有权限未授予
            // 关闭位置服务
            preferenceHelper.locationServiceValue = false
            // TODO 移除第一页（当前位置）天气
            // 弹 dialog 提醒
            homeViewContract!!.onDetectedLocationServicePermissionsDenied()
        }
        // 初始化主页的所有 fragment
        homeViewContract!!.showInitialFragment(isRestored, currentFragmentTag)
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
            preferenceHelper.locationServiceValue = true
            // TODO 添加第一页（当前位置）天气
        }
        // 若未授予权限，不执行任何操作
    }

    private fun switchFragment(toTag: String) {
        if (currentFragmentTag == toTag) return
        homeViewContract!!.switchFragment(currentFragmentTag, toTag)
        currentFragmentTag = toTag
    }

    @Subscribe
    fun onCitySelected(event: CitySelectedEvent) {
        switchFragment(Constant.WEATHER)
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
