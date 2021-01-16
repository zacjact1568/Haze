package net.zackzhang.code.haze.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.widget.Toast
import net.zackzhang.code.haze.App
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.Constant
import java.util.*

object SystemUtil {

    /**
     * 检查 permission 权限是否已授予
     * @return
     * - Android 6.0 以下，总是返回 true TODO 验证
     * - Android 6.0 及以上，若权限未授予，返回 false，否则返回 true
     */
    fun checkPermission(permission: String) = ContextCompat.checkSelfPermission(App.context, permission) == PackageManager.PERMISSION_GRANTED

    /** 检查 permissions 中的权限，只要有一个未授予，返回 false */
    fun checkPermissions(permissions: Array<String>) = permissions.find { !checkPermission(it) } == null

    /** 获取 permissions 中未被授予的权限 */
    fun getNotGrantedPermissions(permissions: Array<String>) = permissions.filterNotTo(mutableListOf()) { checkPermission(it) }.toTypedArray()

    /**
     * 检查 permission 权限是否应该弹需求原因
     * @return
     * - Android 6.0 以下，总是返回 false
     * - Android 6.0 及以上，若之前未弹过授权窗口，或者用户在授权窗口勾选了“不再询问”，或该权限已授予，返回 false，否则返回 true
     * - 若设备不支持该权限，返回 false（暂时不考虑）
     */
    fun shouldShowRequestPermissionRationale(permission: String, activity: Activity) = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

    /**
     * 检查 permissions 中的权限，如果所有未授予的权限都应该弹需求原因，返回 true
     * 这意味着：
     * 1. permissions 中所有未授予的权限都被请求了一次（弹了一次系统授权窗口）
     * 2. 对于每个未授予的权限，用户都没有勾选“不再询问”
     * 3. 设备支持这里面每个未授予的权限
     * 由于 permissions 中所有未授予的权限缺一不可，根据 2、3 点，有理由向用户展示未授予的权限需求原因
     * 这是因为，如果用户对某个权限勾选了“不再询问”，或者设备不支持某个权限，调用 requestPermissions，系统都会直接拒绝 app 对那个权限的请求
     * 反正都会被拒绝，就没有必要向用户展示权限需求原因了
     * 如果某个权限已授予，shouldShowRequestPermissionRationale 也会返回 false
     * 因此，应先将已授予的权限滤除，否则只要有一个权限已授予，即使其他未授予的权限都应该弹需求原因，该函数都会返回 false
     */
    fun shouldShowRequestPermissionsRationale(permissions: Array<String>, activity: Activity) =
            permissions.filterNot { checkPermission(it) }.find { !shouldShowRequestPermissionRationale(it, activity) } == null

    /** 获取 permissions 中应该弹需求原因的权限 */
    fun getShouldShowRequestRationalePermissions(permissions: Array<String>, activity: Activity) = permissions.filterTo(mutableListOf()) { shouldShowRequestPermissionRationale(it, activity) }.toTypedArray()

    val isNetworkAvailable: Boolean
        get() {
            val manager = App.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return manager.activeNetworkInfo != null && manager.activeNetworkInfo.isAvailable
        }

    val versionName: String
        get() {
            val context = App.context
            return try {
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                "null"
            }
        }

    /**
     * 获取此 app 支持的首选语言
     *
     * - 对于 Android 7.0 以下：用户只能设置一种语言，若该语言是 EN、SC、TC 中的任何一个，首选语言就为该语言，否则为 EN
     * - 对于 Android 7.0 及以上：用户可以按优先级设定多个语言，优先级越高且为 EN、SC、TC 中的任何一个语言，首选语言就为该语言，否则为 EN
     *
     * @return ENGLISH、SIMPLIFIED_CHINESE、TRADITIONAL_CHINESE 三者其一
     */
    val preferredLanguage: String
        get() {
            val config = App.context.resources.configuration
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                return locale2String(config.locale) ?: Constant.EN
            } else {
                // 同 LocaleList.getDefault()
                val localeList = config.locales
                (0 until localeList.size())
                        .asSequence()
                        .map { localeList.get(it) }
                        .forEach { locale2String(it)?.let { return it } }
                return Constant.EN
            }
        }

    // 将 Locale 对象与 String 的转换单独提到一个函数，方便管理
    private fun locale2String(locale: Locale) = when (locale) {
        Locale.ENGLISH -> Constant.EN
        Locale.SIMPLIFIED_CHINESE -> Constant.ZH_CN
        Locale.TRADITIONAL_CHINESE -> Constant.ZH_TW
        else -> null
    }

    val locationMode: Int
        get() {
            val manager = App.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gps = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val network = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            return when {
                gps && network -> Constant.LOCATION_MODE_HIGH_ACCURACY
                gps -> Constant.LOCATION_MODE_DEVICE_SENSORS
                network -> Constant.LOCATION_MODE_BATTERY_SAVING
                else -> Constant.LOCATION_MODE_NONE
            }
        }

    val isSystemLocationServiceEnabled
        get() = locationMode != Constant.LOCATION_MODE_NONE

    fun openLink(link: String, activity: Activity, @StringRes failedResId: Int = R.string.toast_no_link_app_found) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        if (intent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(intent)
        } else {
            Toast.makeText(activity, failedResId, Toast.LENGTH_SHORT).show()
        }
    }

    fun sendEmail(email: String, subject: String, context: Context) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, R.string.toast_no_email_app_found, Toast.LENGTH_SHORT).show()
        }
    }
}
