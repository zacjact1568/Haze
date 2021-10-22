package net.zackzhang.code.haze.weather.view.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.util.ResourceUtils.getDimension
import net.zackzhang.code.haze.common.util.ResourceUtils.getInteger

class ColorGradientBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    /**
     * 区间最大值
     * e.g.
     * 设 [3] = <0, color3>，[4] = <5, color4>，[5] = <10, color5>
     * 那么如果 [range] 与 (0, 5] 区间有交集，[colors] 中包含 color4；与 (5, 10] 区间有交集，[colors] 中包含 color5
     */
    private val maxThresholds: Array<Pair<Int, Int>>

    private val barHeight: Int

    private val ringWidth: Int

    /**
     * 数字范围，对应 [colors]
     */
    private var range: IntRange? = null
        // 用 Delegates.observable 会报 maxThresholds 未初始化
        set(value) {
            if (value != field) {
                colors.clear()
                if (value == null) {
                    visibility = GONE
                } else {
                    visibility = VISIBLE
                    for ((threshold, color) in maxThresholds) {
                        if (value.first > threshold) continue
                        colors += color
                        if (value.last <= threshold) break
                    }
                }
                sameColors = false
            }
            field = value
        }

    /**
     * [range] 内的关键数字，指示 ring 的位置
     */
    private var key: Int? = null

    /**
     * bar 的颜色
     */
    private val colors = mutableListOf<Int>()

    private val barPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val ringPaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    /**
     * 再次调用 [checkAndRecreateBarPaintShader] 时 [colors] 是否未改变
     */
    private var sameColors = true

    init {
        resources.obtainTypedArray(R.array.color_gradient_bar_available_colors).run {
            val size = context.getInteger(R.integer.color_gradient_bar_available_colors_size)
            val start = context.getInteger(R.integer.color_gradient_bar_range_start)
            val step = context.getInteger(R.integer.color_gradient_bar_range_step)
            maxThresholds =
                Array(size) {
                    Pair(
                        if (it == size - 1) Int.MAX_VALUE else start + it * step,
                        getColor(it, Color.TRANSPARENT)
                    )
                }
            recycle()
        }
        context.obtainStyledAttributes(attrs, R.styleable.ColorGradientBar).run {
            barHeight = getDimensionPixelSize(
                R.styleable.ColorGradientBar_barHeight,
                context.getDimension(R.dimen.color_gradient_bar_default_bar_height)
            )
            ringWidth = getDimensionPixelSize(
                R.styleable.ColorGradientBar_ringWidth,
                context.getDimension(R.dimen.color_gradient_bar_default_ring_width)
            )
            ringPaint.strokeWidth = ringWidth.toFloat()
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(barHeight + ringWidth * 2, MeasureSpec.EXACTLY)
        )
    }

    override fun onDraw(canvas: Canvas) {
        checkAndRecreateBarPaintShader()
        canvas.drawRoundRect(
            ringWidth.toFloat(),
            ringWidth.toFloat(),
            (measuredWidth - ringWidth).toFloat(),
            (ringWidth + barHeight).toFloat(),
            barHeight / 2F,
            barHeight / 2F,
            barPaint
        )
        if (range != null && key != null) {
            canvas.drawCircle(
                ((key!! - range!!.first) / (range!!.last - range!!.first).toFloat())
                        * (measuredWidth - barHeight - ringWidth * 2)
                        + barHeight / 2F + ringWidth,
                measuredHeight / 2F,
                (measuredHeight - ringWidth) / 2F,
                ringPaint
            )
        }
    }

    fun setData(range: IntRange?, key: Int? = null, @ColorInt ringColor: Int = Color.BLACK) {
        this.range = range
        this.key = key
        ringPaint.color = ringColor
        invalidate()
    }

    /**
     * barPaint.shader 需要根据 ringWidth、measuredWidth、colors 变化而变化
     * 无法更改 LinearGradient 里面的属性，因此只要这三个值变化了，就需要重建 shader
     * 但 ringWidth 创建时就确定了，且该方法在 onDraw 时调用，measuredWidth 已经确定，因此只需根据 colors 变化而变化
     * NOTE：measuredWidth 不变是基于该 View 的 size 不会动态改变
     * e.g. 动态设置 size，或者（转屏等导致）系统重新测量并得到不一样的结果（目前转屏是重建 Activity，所以这种情况不会出现）
     */
    private fun checkAndRecreateBarPaintShader() {
        if (sameColors) return
        barPaint.apply {
            when {
                colors.isEmpty() -> {
                    color = Color.BLACK
                    shader = null
                }
                colors.size == 1 -> {
                    color = colors[0]
                    shader = null
                }
                // colors.size >= 2
                else -> {
                    color = Color.BLACK
                    shader = LinearGradient(
                        ringWidth.toFloat(),
                        0F,
                        (measuredWidth - ringWidth).toFloat(),
                        0F,
                        colors.toIntArray(),
                        null,
                        Shader.TileMode.CLAMP
                    )
                }
            }
        }
        sameColors = true
    }
}