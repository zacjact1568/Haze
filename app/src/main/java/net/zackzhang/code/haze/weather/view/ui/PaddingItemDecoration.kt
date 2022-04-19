package net.zackzhang.code.haze.weather.view.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.zackzhang.code.haze.base.util.ItemDecorationRectInsets
import net.zackzhang.code.haze.base.util.Orientation

open class PaddingItemDecoration(
    protected val left: Int,
    protected val right: Int,
    protected val top: Int,
    protected val bottom: Int,
    protected val getRectInsets: (position: Int) -> ItemDecorationRectInsets,
) : RecyclerView.ItemDecoration() {

    constructor(
        horizontal: Int,
        vertical: Int,
        orientation: Orientation,
    ) : this(horizontal, horizontal, vertical, vertical, orientation)

    constructor(
        left: Int,
        right: Int,
        top: Int,
        bottom: Int,
        orientation: Orientation,
    ) : this(
        left, right, top, bottom, {
            ItemDecorationRectInsets(
                orientation == Orientation.VERTICAL || orientation == Orientation.HORIZONTAL && it == 0,
                true,
                orientation == Orientation.HORIZONTAL || orientation == Orientation.VERTICAL && it == 0,
                true
            )
        }
    )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        if (position < 0) return
        val insets = getRectInsets(position)
        outRect.set(
            insets.left.convert(left),
            insets.top.convert(top),
            insets.right.convert(right),
            insets.bottom.convert(bottom)
        )
    }
}