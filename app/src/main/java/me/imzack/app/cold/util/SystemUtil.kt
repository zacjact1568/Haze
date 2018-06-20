package me.imzack.app.cold.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import me.imzack.app.cold.App
import me.imzack.app.cold.common.Constant
import java.util.*

object SystemUtil {

    fun checkPermission(permission: String) = ContextCompat.checkSelfPermission(App.context, permission) == PackageManager.PERMISSION_GRANTED

    /** 检查 permissions 中的权限，只要有一个未授予，返回 false */
    fun checkPermissions(permissions: Array<String>) = permissions.find { !checkPermission(it) } == null

    /**
     * 检查是否可以弹授权窗口
     * @return
     * - Android 6.0 以下，总是返回 false
     * - Android 6.0 及以上，TODO 若还未弹过授权窗口，返回 false，若用户在授权窗口勾选了“不再询问”，返回 false，否则返回 true
     * - 若设备不支持该权限，返回 false
     */
    fun isPermissionRequestable(permission: String, activity: Activity) = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

    /** 获取 permissions 中未被授予的权限 */
    fun getNotGrantedPermissions(permissions: Array<String>) = permissions.filterNotTo(mutableListOf()) { checkPermission(it) }.toTypedArray()

    /** 获取 permissions 中可请求的权限 */
    fun getRequestablePermissions(permissions: Array<String>, activity: Activity) = permissions.filterTo(mutableListOf()) { isPermissionRequestable(it, activity) }.toTypedArray()

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
}
