package net.zackzhang.code.haze.common.view.ui

import android.content.Context
import android.database.DataSetObserver
import android.util.AttributeSet
import android.widget.LinearLayout
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.view.InlineListAdapter
import kotlin.properties.Delegates

class InlineListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs, 0, R.style.InlineListViewStyle) {

    var adapter by Delegates.observable<InlineListAdapter?>(null) { _, oldValue, newValue ->
        if (oldValue !== newValue) {
            oldValue?.unregisterDataSetObserver(observer)
            requestLayout()
            newValue?.registerDataSetObserver(observer)
        }
    }

    private val gapBetweenItems: Int

    private val observer = object : DataSetObserver() {

        override fun onChanged() {
            requestLayout()
        }
    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.InlineListView).run {
            gapBetweenItems = getDimensionPixelSize(R.styleable.InlineListView_gapBetweenItems, 0)
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        removeAllViewsInLayout()
        val adpt = adapter ?: return
        repeat(adpt.count) {
            addViewInLayout(
                adpt.getCardView(it, this),
                -1,
                generateDefaultLayoutParams().apply {
                    if (it > 0) {
                        topMargin = gapBetweenItems
                    }
                }
            )
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        adapter?.registerDataSetObserver(observer)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        adapter?.unregisterDataSetObserver(observer)
    }
}