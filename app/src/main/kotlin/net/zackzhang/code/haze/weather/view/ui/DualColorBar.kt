package net.zackzhang.code.haze.weather.view.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.util.RoundPosition
import net.zackzhang.code.haze.common.util.drawRoundRect
import net.zackzhang.code.haze.common.util.ensureNotEmpty
import net.zackzhang.code.haze.common.util.getDimension
import net.zackzhang.code.haze.common.util.intersection
import kotlin.properties.Delegates

class DualColorBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val barHeight: Int

    private val barColorStart: Int

    private val barColorEnd: Int

    /**
     * 指示整个 View 的范围
     */
    private var range by Delegates.observable<IntRange?>(null) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            visibility = if (newValue == null) GONE else VISIBLE
        }
    }

    /**
     * [range] 内的关键数字，指示分隔点的位置
     */
    private var key: Int? = null

    /**
     * [range] 内的数字范围，指示 bar 的范围
     */
    private var barRange: IntRange? = null

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.DualColorBar).run {
            barHeight = getDimensionPixelSize(
                R.styleable.DualColorBar_dcb_height,
                context.getDimension(R.dimen.dp_10)
            )
            barColorStart = getColor(
                R.styleable.DualColorBar_dcb_colorStart,
                Color.WHITE
            )
            barColorEnd = getColor(
                R.styleable.DualColorBar_dcb_colorEnd,
                Color.BLACK
            )
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(barHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onDraw(canvas: Canvas) {
        val rg = range ?: return
        val brg = barRange ?: return
        val mw = measuredWidth
        val mh = measuredHeight.toFloat()
        val rad = measuredHeight / 2F
        // bar 起始点（不包含圆角）
        val start = (brg.first - rg.first).toFloat() / (rg.last - rg.first) * (mw - mh) + rad
        // bar 结束点（不包含圆角）
        val end = (brg.last - rg.first).toFloat() / (rg.last - rg.first) * (mw - mh) + rad
        paint.color = barColorStart
        val k = key
        if (k == null) {
            canvas.drawRoundRect(start - rad, 0F, end + rad, mh, rad, rad, paint)
        } else {
            // bar 分隔点
            val divide = (k - rg.first).toFloat() / (rg.last - rg.first) * mw + rad
            if (divide > start && divide < end) {
                // 分隔点在起始、结束点之间，绘制双色 bar
                canvas.drawRoundRect(start - rad, 0F, divide, mh, rad, RoundPosition.START, paint)
                paint.color = barColorEnd
                canvas.drawRoundRect(divide, 0F, end + rad, mh, rad, RoundPosition.END, paint)
            } else if (divide <= start) {
                // 分隔点在起始点上或之前，绘制 end 色 bar
                paint.color = barColorEnd
                canvas.drawRoundRect(start - rad, 0F, end + rad, mh, rad, rad, paint)
            } else if (divide >= end) {
                // 分隔点在起始点上或之后，绘制 start 色 bar
                canvas.drawRoundRect(start - rad, 0F, end + rad, mh, rad, rad, paint)
            }
        }
    }

    fun setData(
        range: IntRange?,
        key: Int? = null,
        barRange: IntRange? = range,
    ) {
        this.range = range?.ensureNotEmpty()
        this.key = key
        this.barRange = range?.let { barRange?.intersection(it) }
        invalidate()
    }
}