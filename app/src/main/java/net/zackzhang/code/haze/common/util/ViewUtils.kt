package net.zackzhang.code.haze.common.util

import android.widget.TextView
import androidx.annotation.ColorInt
import net.zackzhang.code.haze.common.constant.PLACEHOLDER

fun String?.orPlaceholder() = this ?: PLACEHOLDER

fun TextView.updateTextOrPlaceholder(text: Any?, @ColorInt color: Int) {
    this.text = text?.toString() ?: PLACEHOLDER
    setTextColor(color)
}