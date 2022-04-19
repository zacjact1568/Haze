package net.zackzhang.code.haze.settings.view.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.base.util.createColorStateList
import net.zackzhang.code.haze.base.util.reduceSaturation
import kotlin.properties.Delegates

class ColoredSwitch @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SwitchCompat(context, attrs) {

    var checkedColor by Delegates.observable(context.getColor(R.color.colorAccent)) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            val fallback = context.getColor(R.color.gray_300)
            trackTintList = createColorStateList(
                reduceSaturation(newValue, 3),
                reduceSaturation(fallback, 5))
            thumbTintList = createColorStateList(newValue, fallback)
        }
    }
}