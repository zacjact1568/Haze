package me.imzack.app.cold.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.support.v4.content.ContextCompat
import me.imzack.app.cold.App
import me.imzack.app.cold.common.Constant
import java.util.*

object SystemUtil {

    val isPermissionsGranted: Boolean
        get() {
            val context = App.context
            return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }

    val isNetworkAvailable: Boolean
        get() {
            val manager = App.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return manager.activeNetworkInfo != null && manager.activeNetworkInfo.isAvailable
        }

    val versionName: String
        get() {
            val context = App.context
            return try {
                val info = context.packageManager.getPackageInfo(context.packageName, 0)
                info.versionName
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
}
