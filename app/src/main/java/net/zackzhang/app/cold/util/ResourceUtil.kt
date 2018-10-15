package net.zackzhang.app.cold.util

import android.support.annotation.AnimRes
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.view.animation.AnimationUtils
import net.zackzhang.app.cold.App

object ResourceUtil {

    fun getColor(@ColorRes resId: Int) = ContextCompat.getColor(App.context, resId)

    fun getString(@StringRes resId: Int): String = App.context.getString(resId)

    fun getAnimation(@AnimRes resId: Int) = AnimationUtils.loadAnimation(App.context, resId)!!

    /** 通过资源文件名获取 string 资源 id，如果未找到，返回 0 */
    fun getStringResourceId(name: String): Int {
        val context = App.context
        return context.resources.getIdentifier(name, "string", context.packageName)
    }
}