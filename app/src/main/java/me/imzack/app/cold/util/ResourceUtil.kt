package me.imzack.app.cold.util

import android.support.annotation.AnimRes
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.view.animation.AnimationUtils
import me.imzack.app.cold.App

object ResourceUtil {

    fun getColor(@ColorRes resId: Int) = ContextCompat.getColor(App.context, resId)

    fun getString(@StringRes resId: Int) = App.context.getString(resId)!!

    fun getAnimation(@AnimRes resId: Int) = AnimationUtils.loadAnimation(App.context, resId)!!
}