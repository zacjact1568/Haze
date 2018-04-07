package me.imzack.app.cold.view.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import me.imzack.app.cold.R
import me.imzack.app.cold.model.DataManager
import me.imzack.app.cold.util.ResourceUtil
import me.imzack.app.cold.util.StringUtil
import me.imzack.app.cold.util.SystemUtil
import me.imzack.app.cold.view.dialog.MessageDialogFragment
import me.imzack.lib.baseguideactivity.SimpleGuidePageFragment

class LocationGuidePageFragment : SimpleGuidePageFragment() {

    companion object {

        private const val TAG_PRE_REQUEST_PERMISSIONS = "pre_request_permissions"

        fun newInstance(): LocationGuidePageFragment {
            val fragment = LocationGuidePageFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    private val _permissionRequestCode = 0
    /** 需要的权限 */
    private val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    /** 还未授予的权限 */
    private val notGrantedPermissions = SystemUtil.getNotGrantedPermissions(permissions)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isLocationServiceEnabled = DataManager.preferenceHelper.locationServiceValue
        if (!SystemUtil.checkPermissions(permissions) && isLocationServiceEnabled) {
            // 检测到未授权却已打开位置服务，将 preference 中的值强制置为 false
            // 出现这种情况是因为之前已在向导中授权，但向导未结束就退出 app 了，用户又在系统设置中关闭了权限，下次启动 app 又会重新进入向导
            DataManager.preferenceHelper.locationServiceValue = false
            isLocationServiceEnabled = false
        }

        imageResId = R.drawable.ic_place_black_24dp
        imageTint = Color.WHITE
        titleText = StringUtil.addWhiteColorSpan(ResourceUtil.getString(R.string.title_page_location))
        descriptionText = StringUtil.addWhiteColorSpan(ResourceUtil.getString(if (isLocationServiceEnabled) R.string.description_page_location_enabled else R.string.description_page_location_disabled))
        buttonText = if (isLocationServiceEnabled) null else StringUtil.addWhiteColorSpan(ResourceUtil.getString(R.string.btn_page_location))
        buttonBackgroundColor = ResourceUtil.getColor(R.color.colorAccent)
        buttonClickListener = { requestPermissions() }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        if (childFragment.tag == TAG_PRE_REQUEST_PERMISSIONS) {
            (childFragment as MessageDialogFragment).okButtonClickListener = { requestPermissions(notGrantedPermissions, _permissionRequestCode) }
        }
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
                TODO("授权被打断")
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || notGrantedPermissions.isEmpty()) {
            // 说明不需要动态授权，或用户打开 app 之前已手动开启所有权限
            onPermissionsGranted()
        } else {
            // 说明需要动态授权，且开启 app 前没给权限，弹确认授权窗口
            MessageDialogFragment.Builder()
                    .setTitle(R.string.title_dialog_pre_request_permissions)
                    .setMessage(R.string.msg_dialog_pre_request_permissions)
                    .setOkButtonText(R.string.pos_btn_dialog_pre_request_permissions)
                    .showCancelButton()
                    //.setThirdButtonText("Check Permissions")
                    // 必须用 childFragmentManager，否则 onAttachFragment 不会回调
                    .show(childFragmentManager, TAG_PRE_REQUEST_PERMISSIONS)
        }
    }

    private fun onPermissionsGranted() {
        descriptionText = StringUtil.addWhiteColorSpan(getString(R.string.description_page_location_enabled))
        buttonText = null
        DataManager.preferenceHelper.locationServiceValue = true
    }

    private fun onPermissionsDenied() {
        MessageDialogFragment.Builder()
                .setTitle(R.string.title_dialog_permissions_denied)
                .setMessage(R.string.msg_dialog_permissions_denied)
                .setOkButtonText(android.R.string.ok)
                .show(childFragmentManager)
    }
}