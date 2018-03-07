package me.imzack.app.cold.presenter

import android.os.Bundle
import me.imzack.app.cold.App
import me.imzack.app.cold.R
import me.imzack.app.cold.common.Constant
import me.imzack.app.cold.event.CitySelectedEvent
import me.imzack.app.cold.model.DataManager
import me.imzack.app.cold.view.contract.HomeViewContract
import org.greenrobot.eventbus.Subscribe

class HomePresenter(private var homeViewContract: HomeViewContract?) : BasePresenter() {

    private val eventBus = App.eventBus
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
        if (DataManager.preferenceHelper.needGuideValue) {
            homeViewContract!!.startActivity(Constant.GUIDE)
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

    private fun switchFragment(toTag: String) {
        if (currentFragmentTag == toTag) return
        homeViewContract!!.switchFragment(currentFragmentTag, toTag)
        currentFragmentTag = toTag
    }

    @Subscribe
    fun onCitySelected(event: CitySelectedEvent) {
        switchFragment(Constant.WEATHER)
    }
}
