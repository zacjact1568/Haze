package net.zackzhang.code.haze.util

import androidx.annotation.AnimRes
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import android.view.animation.AnimationUtils
import net.zackzhang.code.haze.App

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