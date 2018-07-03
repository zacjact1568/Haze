package net.zackzhang.app.cold.view.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.Fragment
import net.zackzhang.app.cold.R
import net.zackzhang.app.cold.model.DataManager
import net.zackzhang.app.cold.util.SystemUtil
import net.zackzhang.app.cold.view.dialog.MessageDialogFragment

class LocationServicePermissionsFragment : BaseFragment() {

    companion object {

        const val TAG_LOCATION_SERVICE_PERMISSIONS = "location_service_permissions"

        private const val TAG_PRE_REQUEST_PERMISSIONS = "pre_request_permissions"
        private const val TAG_NO_REQUESTABLE_PERMISSIONS = "no_requestable_permissions"

        private const val CODE_REQUEST_PERMISSIONS = 0

        /** 位置服务需要的权限 */
        val PERMISSIONS = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        // TODO 为什么不能初始化，提示没有幕后字段？
        val locationServiceEnabled: Boolean
            get() {
                val preferenceHelper = DataManager.preferenceHelper
                var isLocationServiceEnabled = preferenceHelper.locationServiceValue
                if (SystemUtil.getNotGrantedPermissions(PERMISSIONS).isNotEmpty() && isLocationServiceEnabled) {
                    // 检测到未授权却已打开位置服务，将 preference 中的值强制置为 false
                    // 出现这种情况是因为之前已在向导中授权，但向导未结束就退出 app 了，用户又在系统设置中关闭了权限，下次启动 app 又会重新进入向导
                    preferenceHelper.locationServiceValue = false
                    isLocationServiceEnabled = false
                }
                return isLocationServiceEnabled
            }
    }

    // 动态获取还未授予的权限
    private val notGrantedPermissions
        get() = SystemUtil.getNotGrantedPermissions(PERMISSIONS)

    // 动态获取未授予的权限中可以请求的权限，例如用户还没有勾选“不再询问”
    private val requestableNotGrantedPermissions
        get() = SystemUtil.getRequestablePermissions(notGrantedPermissions, activity!!)

    var permissionsRequestFinishedListener: ((Boolean) -> Unit)? = null

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        when (childFragment.tag) {
            TAG_PRE_REQUEST_PERMISSIONS -> (childFragment as MessageDialogFragment).okButtonClickListener = {
                requestPermissions(requestableNotGrantedPermissions, CODE_REQUEST_PERMISSIONS)
            }
            TAG_NO_REQUESTABLE_PERMISSIONS -> (childFragment as MessageDialogFragment).okButtonClickListener = {
                // 打开应用信息
                startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(Uri.fromParts("package", context!!.packageName, null))
                )
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == CODE_REQUEST_PERMISSIONS) {
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

    fun requestPermissions() {
        when {
            // 说明不需要动态授权，或用户之前已手动开启所有权限
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M || notGrantedPermissions.isEmpty() -> onPermissionsGranted()
            // 说明需要动态授权，且之前没给权限
            // 有可请求的权限，弹确认授权窗口
            requestableNotGrantedPermissions.isNotEmpty() -> MessageDialogFragment.Builder()
                    .setTitle(R.string.title_dialog_pre_request_permissions)
                    .setMessage(R.string.msg_dialog_pre_request_permissions)
                    .setOkButtonText(R.string.pos_btn_dialog_pre_request_permissions)
                    .showCancelButton()
                    //.setThirdButtonText("Check Permissions")
                    // 必须用 childFragmentManager，否则 onAttachFragment 不会回调
                    .show(childFragmentManager, TAG_PRE_REQUEST_PERMISSIONS)
            // 没有可请求的权限
            else -> MessageDialogFragment.Builder()
                    .setTitle(R.string.title_dialog_no_requestable_permissions)
                    .setMessage(R.string.msg_dialog_no_requestable_permissions)
                    .setOkButtonText(R.string.pos_btn_dialog_no_requestable_permissions)
                    .showCancelButton()
                    .show(childFragmentManager, TAG_NO_REQUESTABLE_PERMISSIONS)
        }
    }

    private fun onPermissionsGranted() {
        // 就算后面调用 exit 移除了 LocationServicePermissionsFragment，也不会影响到该回调函数中新建的 Fragment 等
        // 因为回调函数是在宿主类中定义的，和 LocationServicePermissionsFragment 无关
        // 就算要使用 Fragment Manager，使用的也是宿主类的
        permissionsRequestFinishedListener?.invoke(true)
        exit()
    }

    private fun onPermissionsDenied() {
        MessageDialogFragment.Builder()
                .setTitle(R.string.title_dialog_permissions_denied)
                .setMessage(R.string.msg_dialog_permissions_denied)
                .setOkButtonText(android.R.string.ok)
                // 这里必须用 fragmentManager，让 MessageDialogFragment 由 LocationServicePermissionsFragment 的宿主类的 Fragment Manager 来管理
                // 如果使用 childFragmentManager，后面调用 exit 移除 LocationServicePermissionsFragment 后，MessageDialogFragment 无法显示
                // 因为 childFragmentManager 是属于 LocationServicePermissionsFragment 的
                .show(fragmentManager!!)
        permissionsRequestFinishedListener?.invoke(false)
        exit()
    }
}