package net.zackzhang.code.haze.weather.view.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PaddingItemDecoration(
    private val horizontal: Int,
    private val vertical: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (parent.getChildLayoutPosition(view)) {
            RecyclerView.NO_POSITION -> return
            0 -> outRect.set(horizontal, vertical, horizontal, vertical)
            else -> outRect.set(0, vertical, horizontal, vertical)
        }
    }
}