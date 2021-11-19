package net.zackzhang.code.haze.weather.view.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PaddingItemDecoration(
    private val horizontal: Int,
    private val top: Int,
    private val bottom: Int,
) : RecyclerView.ItemDecoration() {

    constructor(horizontal: Int, vertical: Int): this(horizontal, vertical, vertical)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (parent.getChildLayoutPosition(view)) {
            RecyclerView.NO_POSITION -> return
            0 -> outRect.set(horizontal, top, horizontal, bottom)
            else -> outRect.set(0, top, horizontal, bottom)
        }
    }
}