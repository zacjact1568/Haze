package me.imzack.app.cold.view.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast

import me.imzack.app.cold.R

import me.imzack.app.cold.model.DataManager
import me.imzack.app.cold.presenter.GuidePresenter
import me.imzack.app.cold.util.ResourceUtil
import me.imzack.app.cold.util.StringUtil
import me.imzack.app.cold.util.SystemUtil
import me.imzack.app.cold.view.contract.GuideViewContract
import me.imzack.app.cold.view.dialog.MessageDialogFragment
import me.imzack.lib.baseguideactivity.BaseGuideActivity
import me.imzack.lib.baseguideactivity.SimpleGuidePageFragment

class GuideActivity : BaseGuideActivity(), GuideViewContract {

    companion object {

        fun start(activity: Activity) {
            activity.startActivityForResult(Intent(activity, GuideActivity::class.java), 0)
        }
    }

    private val guidePresenter = GuidePresenter(this)

    private val locationPageFragment by lazy {
        var isLocationServiceEnabled = DataManager.preferenceHelper.locationServiceValue
        if (!SystemUtil.isPermissionsGranted && isLocationServiceEnabled) {
            // 检测到未授权却已打开位置服务，将 preference 中的值强制置为 false
            // 出现这种情况是因为之前已在向导中授权，但向导未结束就退出 app 了，用户又在系统设置中关闭了权限，下次启动 app 又会重新进入向导
            DataManager.preferenceHelper.locationServiceValue = false
            isLocationServiceEnabled = false
        }
        SimpleGuidePageFragment.newInstance(
                R.drawable.pic_cloud,
                StringUtil.addWhiteColorSpan(getString(R.string.title_page_location)),
                StringUtil.addWhiteColorSpan(getString(if (isLocationServiceEnabled) R.string.dscpt_enabled_page_location else R.string.dscpt_disabled_page_location)),
                if (isLocationServiceEnabled) null else StringUtil.addWhiteColorSpan(getString(R.string.btn_page_location)),
                ResourceUtil.getColor(R.color.colorAccent),
                object : SimpleGuidePageFragment.OnButtonClickListener {
                    override fun onClick(v: View) {
                        requestLocationPermission()
                    }
                }
        )
    }

    private val _permissionRequestCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        guidePresenter.attach()
    }

    override fun provideFragmentList() = listOf(
            SimpleGuidePageFragment.newInstance(
                    R.drawable.pic_cloud,
                    StringUtil.addWhiteColorSpan(getString(R.string.title_page_welcome)),
                    StringUtil.addWhiteColorSpan(getString(R.string.dscpt_page_welcome))
            ),
            locationPageFragment,
            SimpleGuidePageFragment.newInstance(
                    R.drawable.pic_cloud,
                    StringUtil.addWhiteColorSpan(getString(R.string.title_page_ready)),
                    StringUtil.addWhiteColorSpan(getString(R.string.dscpt_page_ready))
            )
    )

    override fun onBackPressedOnce() {
        showToast(R.string.toast_double_press_exit)
    }

    override fun onBackPressedTwice() {
        guidePresenter.notifyEndingGuide(false)
    }

    override fun onLastPageTurned() {
        guidePresenter.notifyEndingGuide(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        guidePresenter.detach()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == _permissionRequestCode) {
            if (grantResults.isNotEmpty()) {
                if (grantResults.contains(PackageManager.PERMISSION_DENIED)) {
                    // 存在权限未授予
                    onPermissionsDenied()
                } else {
                    // 说明全部权限都已授予
                    onPermissionsGranted()
                }
            } else {
                //TODO 授权被打断
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun showInitialView() {
        setBackgroundColor(ResourceUtil.getColor(R.color.colorPrimary))
        setStartButtonColor(ResourceUtil.getColor(R.color.colorPrimaryDark))
        setEndButtonColor(ResourceUtil.getColor(R.color.colorPrimaryDark))
    }

    override fun exitWithResult(isNormally: Boolean) {
        setResult(if (isNormally) RESULT_OK else RESULT_CANCELED)
        exit()
    }

    override fun showToast(msgResId: Int) {
        Toast.makeText(this, msgResId, Toast.LENGTH_SHORT).show()
    }

    override fun exit() {
        finish()
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || SystemUtil.isPermissionsGranted) {
            // 说明不需要动态授权，或用户打开 app 之前已手动开启所有权限
            onPermissionsGranted()
        } else {
            // 说明需要动态授权，且开启 app 前没给权限，弹系统授权窗口
            requestPermissions(
                    arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    _permissionRequestCode
            )
        }
    }

    private fun onPermissionsGranted() {
        Toast.makeText(this, R.string.toast_ls_success, Toast.LENGTH_SHORT).show()
        locationPageFragment.mDescriptionText = StringUtil.addWhiteColorSpan(getString(R.string.dscpt_enabled_page_location))
        locationPageFragment.mButtonText = null
        DataManager.preferenceHelper.locationServiceValue = true
        // TODO 开始获取位置
    }

    private fun onPermissionsDenied() {
        Toast.makeText(this, R.string.toast_ls_failure, Toast.LENGTH_SHORT).show()
        MessageDialogFragment.newInstance(
                getString(R.string.msg_dialog_ls_failure),
                getString(R.string.title_dialog_ls_failure),
                getString(android.R.string.ok),
                showCancelButton = false
        ).show(supportFragmentManager)
    }
}
