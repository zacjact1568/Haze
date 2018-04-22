package me.imzack.app.cold.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*
import me.imzack.app.cold.R
import me.imzack.app.cold.common.Constant
import me.imzack.app.cold.presenter.HomePresenter
import me.imzack.app.cold.view.contract.HomeViewContract
import me.imzack.app.cold.view.dialog.MessageDialogFragment
import me.imzack.app.cold.view.fragment.CitiesFragment
import me.imzack.app.cold.view.fragment.LocationServicePermissionsFragment
import me.imzack.app.cold.view.fragment.WeatherFragment

class HomeActivity : BaseActivity(), HomeViewContract {

    companion object {

        private const val TAG_DETECTED_LOC_SER_PER_DENIED = "detected_loc_ser_per_denied"

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
            TAG_DETECTED_LOC_SER_PER_DENIED -> (fragment as MessageDialogFragment).okButtonClickListener = {
                var lspFragment = supportFragmentManager.findFragmentByTag(LocationServicePermissionsFragment.TAG_LOCATION_SERVICE_PERMISSIONS) as LocationServicePermissionsFragment?
                if (lspFragment == null) {
                    lspFragment = LocationServicePermissionsFragment()
                    supportFragmentManager.beginTransaction().add(lspFragment, LocationServicePermissionsFragment.TAG_LOCATION_SERVICE_PERMISSIONS).commitNow()
                }
                lspFragment.requestPermissions()
            }
            LocationServicePermissionsFragment.TAG_LOCATION_SERVICE_PERMISSIONS ->
                (fragment as LocationServicePermissionsFragment).permissionsRequestFinishedListener = {
                    homePresenter.notifyLocationServicePermissionsRequestFinished(it)
                }
        }
    }

    override fun showInitialView() {
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
    }

    override fun showInitialFragment(restored: Boolean, shownTag: String) {
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

        updateAfterUpdatingFragment(shownTag)
    }

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun switchFragment(fromTag: String, toTag: String) {
        if (!isFragmentExist(toTag)) {
            // 如果要显示的 fragment 还未创建，创建
            supportFragmentManager.beginTransaction().hide(fromTag).add(toTag).commit()
        } else if (!isFragmentShowing(toTag)) {
            // 如果要显示的 fragment 已创建但未显示，显示
            supportFragmentManager.beginTransaction().hide(fromTag).show(toTag).commit()
        }
        // 如果要显示的 fragment 已创建且已显示，不做任何操作

        updateAfterUpdatingFragment(toTag)
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

    override fun onDetectedLocationServicePermissionsDenied() {
        MessageDialogFragment.Builder()
                .setTitle(R.string.title_dialog_detected_location_service_permissions_denied)
                .setMessage(R.string.msg_dialog_detected_location_service_permissions_denied)
                .setOkButtonText(R.string.pos_btn_dialog_detected_location_service_permissions_denied)
                .showCancelButton()
                .show(supportFragmentManager, TAG_DETECTED_LOC_SER_PER_DENIED)
    }

    private fun updateAfterUpdatingFragment(tag: String) {
        val title: String
        val visibility: Int
        val checkedItemId: Int
        when (tag) {
            Constant.WEATHER -> {
                title = " "
                visibility = View.GONE
                checkedItemId = R.id.nav_weather
            }
            Constant.CITIES -> {
                title = getString(R.string.title_fragment_cities)
                visibility = View.VISIBLE
                checkedItemId = R.id.nav_cities
            }
            // 在这里添加新 fragment
            else -> throw IllegalArgumentException("The argument tag cannot be \"$tag\"")
        }
        vToolbar.title = title
        vAddFab.visibility = visibility
        // FAB 归位
        vAddFab.translationY = 0f
        vNavigator.setCheckedItem(checkedItemId)
    }

    private fun findFragment(tag: String): Fragment? = supportFragmentManager.findFragmentByTag(tag)

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
                    else -> throw IllegalArgumentException("The argument tag cannot be \"$tag\"")
                },
                tag
        )
        return this
    }

    private fun FragmentTransaction.hide(tag: String): FragmentTransaction {
        hide(findFragment(tag))
        return this
    }

    private fun FragmentTransaction.show(tag: String): FragmentTransaction {
        show(findFragment(tag))
        return this
    }
}
