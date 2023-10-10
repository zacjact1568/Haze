package net.zackzhang.code.haze.common.util

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.annotation.ColorInt

@ColorInt
fun reduceSaturation(@ColorInt color: Int, by: Int): Int {
    checkColorLegalityForHsvConversion(color)
    val hsv = FloatArray(3)
    Color.colorToHSV(color, hsv)
    hsv[1] = hsv[1] / by
    return Color.HSVToColor(hsv)
}

@ColorInt
fun increaseBrightness(@ColorInt color: Int, by: Int): Int {
    checkColorLegalityForHsvConversion(color)
    val hsv = FloatArray(3)
    Color.colorToHSV(color, hsv)
    hsv[2] = hsv[2] * by
    return Color.HSVToColor(hsv)
}

fun createColorStateList(@ColorInt checked: Int, @ColorInt fallback: Int) =
    ColorStateList(arrayOf(
        intArrayOf(android.R.attr.state_checked),
        intArrayOf(),
        // Other states
    ), intArrayOf(checked, fallback))


private fun checkColorLegalityForHsvConversion(@ColorInt color: Int) {
    if (color shr 24 == -1) return
    // 有不透明度的话 HSV 转换貌似会出错
    throw IllegalArgumentException("Parameter color should not contain alpha channel")
}