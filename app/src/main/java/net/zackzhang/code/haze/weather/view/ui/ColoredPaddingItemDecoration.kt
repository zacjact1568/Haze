package net.zackzhang.code.haze.weather.view.ui

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import net.zackzhang.code.haze.common.util.ItemDecorationRectInsets

class ColoredPaddingItemDecoration(
    left: Int,
    right: Int,
    top: Int,
    bottom: Int,
    getRectInsets: (position: Int) -> ItemDecorationRectInsets,
    @param:ColorInt
    private val color: Int,
    @param:ColorInt
    private val backgroundColor: Int,
    private val needBackground: (position: Int) -> Boolean,
) : PaddingItemDecoration(left, right, top, bottom, getRectInsets) {

    private val paint = Paint()

    constructor(
        all: Int,
        getRectInsets: (position: Int) -> ItemDecorationRectInsets,
        @ColorInt color: Int,
        needBackground: (position: Int) -> Boolean,
    ) : this(all, all, all, all, getRectInsets, color, color, needBackground)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.children.forEach {
            val position = parent.getChildLayoutPosition(it)
            if (position < 0) return@forEach
            val insets = getRectInsets(position)
            val leftOuterEdge = (it.left - insets.left.convert(left)).toFloat()
            val leftInnerEdge = it.left.toFloat()
            val rightOuterEdge = (it.right + insets.right.convert(right)).toFloat()
            val rightInnerEdge = it.right.toFloat()
            val topOuterEdge = (it.top - insets.top.convert(top)).toFloat()
            val topInnerEdge = it.top.toFloat()
            val bottomOuterEdge = (it.bottom + insets.bottom.convert(bottom)).toFloat()
            val bottomInnerEdge = it.bottom.toFloat()
            paint.color = color
            // left side & top/bottom-left corner
            if (insets.left.exist) {
                c.drawRect(leftOuterEdge, topOuterEdge, leftInnerEdge, bottomOuterEdge, paint)
            }
            // right side & top/bottom-right corner
            if (insets.right.exist) {
                c.drawRect(rightInnerEdge, topOuterEdge, rightOuterEdge, bottomOuterEdge, paint)
            }
            // top side
            if (insets.top.exist) {
                c.drawRect(leftInnerEdge, topOuterEdge, rightInnerEdge, topInnerEdge, paint)
            }
            // bottom side
            if (insets.bottom.exist) {
                c.drawRect(leftInnerEdge, bottomInnerEdge, rightInnerEdge, bottomOuterEdge, paint)
            }
            if (needBackground(position)) {
                paint.color = backgroundColor
                // background area
                c.drawRect(leftInnerEdge, topInnerEdge, rightInnerEdge, bottomInnerEdge, paint)
            }
        }
    }
}