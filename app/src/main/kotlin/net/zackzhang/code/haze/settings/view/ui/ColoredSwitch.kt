package net.zackzhang.code.haze.settings.view.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.util.createColorStateList
import net.zackzhang.code.haze.common.util.increaseBrightness
import net.zackzhang.code.haze.common.util.isInDarkMode
import net.zackzhang.code.haze.common.util.reduceSaturation
import kotlin.properties.Delegates

class ColoredSwitch @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SwitchCompat(context, attrs) {

    var checkedColor by Delegates.observable(context.getColor(R.color.accent)) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            // 不要用有不透明度的颜色，否则可以透过圆形区域看到药丸
            // 药丸区域颜色
            val darkMode = context.isInDarkMode
            trackTintList = if (darkMode) {
                createColorStateList(
                    increaseBrightness(newValue, 2),
                    context.getColor(R.color.gray_700),
                )
            } else {
                createColorStateList(
                    reduceSaturation(newValue, 2),
                    context.getColor(R.color.gray_300),
                )
            }
            // 圆形区域颜色
            thumbTintList = if (darkMode) {
                createColorStateList(
                    increaseBrightness(newValue, 3),
                    context.getColor(R.color.gray_800),
                )
            } else {
                createColorStateList(
                    newValue,
                    context.getColor(R.color.gray_400),
                )
            }
        }
    }
}