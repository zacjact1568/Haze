package net.zackzhang.code.haze.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.Constant
import net.zackzhang.code.haze.presenter.HomePresenter
import net.zackzhang.code.haze.view.contract.HomeViewContract
import net.zackzhang.code.haze.view.dialog.MessageDialogFragment
import net.zackzhang.code.haze.view.fragment.CitiesFragment
import net.zackzhang.code.haze.view.fragment.LocationServicePermissionsFragment
import net.zackzhang.code.haze.view.fragment.WeatherFragment

class HomeActivity : BaseActivity(), HomeViewContract {

    companion object {

        private const val TAG_LOCATION_SERVICE_PERMISSIONS = "location_service_permissions"
        private const val TAG_PERMISSIONS_DENIED = "permissions_denied"

        fun start(context: Context) {
            context.startActivity(Intent(context, HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }

    private val homePresenter = HomePresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homePresenter.attach()
    }

    // 此函数可能只有重建 activity 的时候才会回调，正常启动不会回调
    // 也就是说 savedInstanceState 不会为空，源码中此参数没有 @Nullable 标注，印证了这一点
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        homePresenter.notifyInstanceStateRestored(savedInstanceState.getString(Constant.CURRENT_FRAGMENT, Constant.WEATHER))
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        homePresenter.notifyStartingUpCompleted()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        homePresenter.notifySavingInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        homePresenter.detach()
    }

    override fun onBackPressed() {
        homePresenter.notifyBackPressed(
                vDrawerLayout.isDrawerOpen(GravityCompat.START),
                isFragmentShowing(Constant.WEATHER)
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            RESULT_OK -> { }
            RESULT_CANCELED -> exit()
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)

        when (fragment.tag) {
            TAG_LOCATION_SERVICE_PERMISSIONS -> (fragment as LocationServicePermissionsFragment).permissionsRequestFinishedListener = {
                homePresenter.notifyLocationServicePermissionsRequestFinished(it)
            }
            TAG_PERMISSIONS_DENIED -> (fragment as MessageDialogFragment).thirdButtonClickListener = {
                homePresenter.notifyDisablingLocationService()
            }
        }
    }

    override fun showInitialView(currentFragmentTag: String, isCityEmpty: Boolean) {
        setContentView(R.layout.activity_home)

        setSupportActionBar(vToolbar)

        val toggle = ActionBarDrawerToggle(this, vDrawerLayout, vToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        vDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        vNavigator.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_weather -> homePresenter.notifySwitchingFragment(Constant.WEATHER)
                R.id.nav_cities -> homePresenter.notifySwitchingFragment(Constant.CITIES)
                R.id.nav_settings -> startActivity(Constant.SETTINGS)
                R.id.nav_about -> startActivity(Constant.ABOUT)
            }
            vDrawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        vAddFab.setOnClickListener { startActivity(Constant.ADD_CITY) }

        onCityEmptyStateChanged(isCityEmpty, currentFragmentTag)
    }

    override fun showInitialFragment(restored: Boolean, shownTag: String, isCityEmpty: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        if (restored) {
            // 如果 activity 重建过，所有 fragment 都会变为显示状态并且重叠在一起，需要隐藏除 shownTag 对应的 fragment 外的其他 fragment
            supportFragmentManager.fragments
                    .map { it.tag }
                    // 不隐藏 tag 为 null 的 fragment，因为这是 WeatherFragment 的子页（每个城市）
                    .filter { it != null && it != shownTag }
                    .forEach { it?.let { transaction.hide(it) } }
        } else {
            // 如果 activity 正常启动，仅添加 shownTag 对应的 fragment
            transaction.add(shownTag)
        }
        transaction.commit()

        updateAfterUpdatingFragment(shownTag, isCityEmpty)
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun switchFragment(fromTag: String, toTag: String, isCityEmpty: Boolean) {
        if (!isFragmentExist(toTag)) {
            // 如果要显示的 fragment 还未创建，创建
            supportFragmentManager.beginTransaction().hide(fromTag).add(toTag).commit()
        } else if (!isFragmentShowing(toTag)) {
            // 如果要显示的 fragment 已创建但未显示，显示
            supportFragmentManager.beginTransaction().hide(fromTag).show(toTag).commit()
        }
        // 如果要显示的 fragment 已创建且已显示，不做任何操作

        updateAfterUpdatingFragment(toTag, isCityEmpty)
    }

    override fun startActivity(tag: String) {
        when (tag) {
            Constant.GUIDE -> GuideActivity.start(this)
            Constant.SETTINGS -> SettingsActivity.start(this)
            Constant.ABOUT -> AboutActivity.start(this)
            Constant.ADD_CITY -> CityAddActivity.start(this)
        }
    }

    override fun closeDrawer() {
        vDrawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onPressBackKey() {
        super.onBackPressed()
    }

    override fun onDetectedNetworkNotAvailable() {
        Snackbar.make(vContentLayout, R.string.text_network_not_available, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_settings) { startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS)) }
                .show()
    }

    override fun onDetectedSystemLocationServiceDisabled() {
        Snackbar.make(vContentLayout, R.string.text_location_not_available, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_settings) { startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .show()
    }

    override fun onDetectedNoEnoughPermissionsGranted() {
        supportFragmentManager.beginTransaction().add(LocationServicePermissionsFragment(), TAG_LOCATION_SERVICE_PERMISSIONS).commit()
    }

    override fun onLocationServicePermissionsDenied() {
        Snackbar.make(vContentLayout, R.string.text_no_enough_permissions_granted, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_detail) {
                    MessageDialogFragment.Builder()
                            .setTitle(R.string.title_dialog_permissions_denied)
                            .setMessage(R.string.msg_dialog_permissions_denied)
                            .setOkButtonText(android.R.string.ok)
                            .setThirdButtonText(R.string.neu_btn_dialog_permissions_denied)
                            .show(supportFragmentManager, TAG_PERMISSIONS_DENIED)
                }
                .show()
    }

    override fun onCityEmptyStateChanged(isEmpty: Boolean, currentFragmentTag: String) {
        updateAfterUpdatingFragment(currentFragmentTag, isEmpty)
    }

    private fun updateAfterUpdatingFragment(tag: String, isCityEmpty: Boolean) {
        val toolbarTitle: String
        val isFabVisible: Boolean
        val navCheckedItemId: Int
        when (tag) {
            Constant.WEATHER -> {
                toolbarTitle = if (isCityEmpty) getString(R.string.title_fragment_weather) else " "
                isFabVisible = false
                navCheckedItemId = R.id.nav_weather
            }
            Constant.CITIES -> {
                toolbarTitle = getString(R.string.title_fragment_cities)
                isFabVisible = !isCityEmpty
                navCheckedItemId = R.id.nav_cities
            }
            // 在这里添加新 fragment
            else -> throw IllegalArgumentException("The argument \"tag\" cannot be \"$tag\"")
        }
        vToolbar.title = toolbarTitle
        if (isFabVisible) vAddFab.show() else vAddFab.hide()
        // FAB 归位
        vAddFab.translationY = 0f
        vNavigator.setCheckedItem(navCheckedItemId)
    }

    private fun findFragment(tag: String) = supportFragmentManager.findFragmentByTag(tag)

    private fun isFragmentExist(tag: String) = findFragment(tag) != null

    private fun isFragmentShowing(tag: String): Boolean {
        val fragment = findFragment(tag)
        return fragment != null && !fragment.isHidden
    }

    private fun FragmentTransaction.add(tag: String): FragmentTransaction {
        add(
                R.id.vContentLayout,
                when (tag) {
                    Constant.WEATHER -> WeatherFragment()
                    Constant.CITIES -> CitiesFragment()
                    // 在这里添加新 fragment
                    else -> throw IllegalArgumentException("The argument \"tag\" cannot be \"$tag\"")
                },
                tag
        )
        return this
    }

    private fun FragmentTransaction.hide(tag: String): FragmentTransaction {
        hide(findFragment(tag) ?: throw IllegalArgumentException("No Fragment with tag \"$tag\" found"))
        return this
    }

    private fun FragmentTransaction.show(tag: String): FragmentTransaction {
        show(findFragment(tag) ?: throw IllegalArgumentException("No Fragment with tag \"$tag\" found"))
        return this
    }
}
