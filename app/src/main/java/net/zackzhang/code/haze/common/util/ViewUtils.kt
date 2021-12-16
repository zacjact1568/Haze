package net.zackzhang.code.haze.common.util

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.widget.TextView
import androidx.annotation.ColorInt
import net.zackzhang.code.haze.common.constant.PLACEHOLDER

fun Int?.toStringOrPlaceholder() = this?.toString() ?: PLACEHOLDER

fun String?.orPlaceholder() = this ?: PLACEHOLDER

fun TextView.updateTextOrPlaceholder(text: Any?, @ColorInt color: Int) {
    this.text = text?.toString() ?: PLACEHOLDER
    setTextColor(color)
}

fun Canvas.drawRoundRect(
    start: Float,
    top: Float,
    end: Float,
    bottom: Float,
    radii: Float,
    position: RoundPosition,
    paint: Paint
) {
    save()
    val r = RectF(start, top, end, bottom)
    clipRect(r)
    when (position) {
        RoundPosition.START -> r.right += radii
        RoundPosition.END -> r.left -= radii
        RoundPosition.TOP -> r.bottom += radii
        RoundPosition.BOTTOM -> r.top -= radii
    }
    drawRoundRect(r, radii, radii, paint)
    restore()
}

enum class RoundPosition {
    START, END, TOP, BOTTOM,
}