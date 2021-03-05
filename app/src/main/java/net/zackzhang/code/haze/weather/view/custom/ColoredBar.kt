package net.zackzhang.code.haze.weather.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import androidx.collection.arrayMapOf

class ColoredBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val map = arrayMapOf(
        -10 to Color.CYAN,
        0 to Color.BLUE,
        10 to Color.GREEN,
        20 to Color.YELLOW,
        30 to Color.RED,
    )

    var tempRange: IntRange? = null
        set(value) {
            if (value != null) {
                map.forEach { (k, v) ->
                    if (value.first > k) {
                        colors += v
                    }
                }
            }
            field = value
        }

    var temp: Int? = null

    private var colors = mutableListOf<Int>()

    private val grad by lazy {
        LinearGradient(0F, 0F, measuredWidth.toFloat(), 0F, colors.toIntArray(), null, Shader.TileMode.CLAMP)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // TODO
    }

    override fun onDraw(canvas: Canvas?) {
        val g = grad
    }
}