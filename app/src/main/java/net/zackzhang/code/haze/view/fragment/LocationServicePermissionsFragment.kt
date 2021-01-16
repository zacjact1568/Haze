package net.zackzhang.code.haze.view.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.model.DataManager
import net.zackzhang.code.haze.util.LogUtil
import net.zackzhang.code.haze.util.SystemUtil
import net.zackzhang.code.haze.view.dialog.MessageDialogFragment

class LocationServicePermissionsFragment : BaseFragment() {

    companion object {

        private const val TAG_PRE_REQUEST_PERMISSIONS = "pre_request_permissions"
        private const val TAG_NEVER_REQUEST_PERMISSIONS_AGAIN = "never_request_permissions_again"

        private const val CODE_REQUEST_PERMISSIONS = 0

        /** 高德位置服务需要的所有权限 */
        val PERMISSIONS = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private val preferenceHelper = DataManager.preferenceHelper

    var permissionsRequestFinishedListener: ((Boolean) -> Unit)? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 视图加载完成后，立即检查权限情况
        when {
            // 说明不需要动态授权，或用户之前已手动开启所有权限
            // 实际上这个分支不会执行，因为检测到有权限未授予才会创建这个 fragment
            SystemUtil.checkPermissions(PERMISSIONS) -> onPermissionsGranted()
            // 说明需要动态授权，且之前没给权限
            // 对于所有未授予的权限，shouldShowRequestPermissionRationale 都为 true，有必要弹权限需求原因窗口
            SystemUtil.shouldShowRequestPermissionsRationale(PERMISSIONS, activity!!) -> MessageDialogFragment.Builder()
                    .setTitle(R.string.title_dialog_pre_request_permissions)
                    .setMessage(R.string.msg_dialog_pre_request_permissions)
                    .setOkButtonText(R.string.pos_btn_dialog_pre_request_permissions)
                    .showCancelButton()
                    //.setThirdButtonText("Check Permissions")
                    // 必须用 childFragmentManager，否则 onAttachFragment 不会回调
                    .show(childFragmentManager, TAG_PRE_REQUEST_PERMISSIONS)
            // 对于至少一个未授予的权限，shouldShowRequestPermissionRationale 为 false，没有必要弹权限需求原因窗口
            // 在这里判断是否已经弹过系统授权窗口
            // 如果弹过，则表示对于某个权限，用户勾选了“不再询问”再选择拒绝，或设备不支持某个权限（暂不考虑）
            preferenceHelper.haveRequestedLocationPermissionsValue -> MessageDialogFragment.Builder()
                    .setTitle(R.string.title_dialog_never_request_permissions_again)
                    .setMessage(R.string.msg_dialog_never_request_permissions_again)
                    .setOkButtonText(R.string.pos_btn_dialog_never_request_permissions_again)
                    .showCancelButton()
                    .show(childFragmentManager, TAG_NEVER_REQUEST_PERMISSIONS_AGAIN)
            // 如果没弹过系统授权窗口，则是首次请求权限，直接弹系统授权窗口
            else -> requestPermissions()
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        when (childFragment.tag) {
            TAG_PRE_REQUEST_PERMISSIONS -> {
                childFragment as MessageDialogFragment
                childFragment.okButtonClickListener = { requestPermissions() }
                childFragment.cancelListener = { exit() }
            }
            TAG_NEVER_REQUEST_PERMISSIONS_AGAIN -> {
                childFragment as MessageDialogFragment
                childFragment.okButtonClickListener = {
                    // 打开应用信息
                    startActivity(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    .setData(Uri.fromParts("package", context!!.packageName, null))
                    )
                }
                childFragment.cancelListener = { exit() }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == CODE_REQUEST_PERMISSIONS) {
            if (grantResults.size == PERMISSIONS.size) {
                if (grantResults.contains(PackageManager.PERMISSION_DENIED)) {
                    // 存在权限未授予
                    onPermissionsDenied()
                } else {
                    // 说明全部权限都已授予
                    onPermissionsGranted()
                }
                preferenceHelper.haveRequestedLocationPermissionsValue = true
            } else {
                LogUtil.e("Permissions granting was interrupted")
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun requestPermissions() {
        requestPermissions(PERMISSIONS, CODE_REQUEST_PERMISSIONS)
    }

    private fun onPermissionsGranted() {
        permissionsRequestFinishedListener?.invoke(true)
        exit()
    }

    private fun onPermissionsDenied() {
        permissionsRequestFinishedListener?.invoke(false)
        exit()
    }
}