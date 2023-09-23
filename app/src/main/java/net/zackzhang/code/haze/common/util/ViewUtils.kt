package net.zackzhang.code.haze.common.util

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import net.zackzhang.code.haze.common.constant.PLACEHOLDER

enum class Orientation {
    HORIZONTAL,
    VERTICAL,
}

enum class Direction {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM,
}

enum class ItemDecorationInset(val convert: (size: Int) -> Int) {

    FULL({ it }),
    HALF({ it / 2 }),
    NONE({ 0 }),
    ;

    val exist get() = this != NONE
}

data class ItemDecorationRectInsets(
    val left: ItemDecorationInset,
    val right: ItemDecorationInset,
    val top: ItemDecorationInset,
    val bottom: ItemDecorationInset,
) {

    constructor(left: Boolean, right: Boolean, top: Boolean, bottom: Boolean) : this(
        if (left) ItemDecorationInset.FULL else ItemDecorationInset.NONE,
        if (right) ItemDecorationInset.FULL else ItemDecorationInset.NONE,
        if (top) ItemDecorationInset.FULL else ItemDecorationInset.NONE,
        if (bottom) ItemDecorationInset.FULL else ItemDecorationInset.NONE,
    )
}

var ImageView.tintColor
    get() = imageTintList?.defaultColor ?: Color.TRANSPARENT
    set(value) {
        imageTintList = ColorStateList.valueOf(value)
    }

var View.scale
    get() = run {
        assert(scaleX == scaleY)
        scaleX
    }
    set(value) {
        scaleX = value
        scaleY = value
    }

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

inline fun <T> Orientation.switch(horizontal: () -> T, vertical: () -> T) =
    when (this) {
        Orientation.HORIZONTAL -> horizontal()
        Orientation.VERTICAL -> vertical()
    }

fun showToast(@StringRes id: Int) {
    Toast.makeText(context, id, Toast.LENGTH_SHORT).show()
}

fun LottieAnimationView.resumeAnimationWhenPause() {
    if (!isVisible || isAnimating) return
    resumeAnimation()
}

fun LottieAnimationView.pauseAnimationWhenPlaying() {
    if (!isVisible || !isAnimating) return
    pauseAnimation()
}