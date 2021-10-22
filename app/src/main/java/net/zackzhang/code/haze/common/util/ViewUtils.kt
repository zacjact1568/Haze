package net.zackzhang.code.haze.common.util

import android.widget.TextView
import androidx.annotation.ColorInt
import net.zackzhang.code.haze.common.Constants

object ViewUtils {

    fun Int?.toStringOrPlaceholder() = this?.toString() ?: Constants.PLACEHOLDER

    fun String?.orPlaceholder() = this ?: Constants.PLACEHOLDER
}

fun TextView.updateText(text: String, @ColorInt color: Int) {
    this.text = text
    setTextColor(color)
}