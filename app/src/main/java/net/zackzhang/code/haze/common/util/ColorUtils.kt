package net.zackzhang.code.haze.common.util

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.annotation.ColorInt

@ColorInt
fun reduceSaturation(@ColorInt color: Int, by: Int): Int {
    val hsv = FloatArray(3)
    Color.colorToHSV(color, hsv)
    hsv[1] = hsv[1] / by
    return Color.HSVToColor(hsv)
}

fun createColorStateList(@ColorInt checked: Int, @ColorInt fallback: Int) =
    ColorStateList(arrayOf(
        intArrayOf(android.R.attr.state_checked),
        intArrayOf(),
        // Other states
    ), intArrayOf(checked, fallback))