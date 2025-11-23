package net.zackzhang.code.haze.components.spring

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.NestedScrollingParent3
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.utils.Orientation
import net.zackzhang.code.haze.utils.withStyledAttributes

class SpringLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), NestedScrollingParent3 {

    private val orientation = context.withStyledAttributes(attrs, R.styleable.SpringLayout) {
        getInt(
            R.styleable.SpringLayout_orientation,
            Orientation.VERTICAL.ordinal
        ).let {
            if (it == 0) Orientation.HORIZONTAL else Orientation.VERTICAL
        }
    }

    private val delegate = SpringNestedScrollingDelegate(this, orientation)

    override fun computeScroll() {
        super.computeScroll()
        delegate.computeScroll()
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        delegate.onScrollChanged(oldl, oldt, l, t)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return delegate.onStartNestedScroll(child, target, axes, type)
                || super.onStartNestedScroll(child, target, axes)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int): Boolean {
        return delegate.onStartNestedScroll(child, target, axes, -1)
                || super.onStartNestedScroll(child, target, axes)
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        super.onNestedScrollAccepted(child, target, axes)
        delegate.onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        super.onNestedScrollAccepted(child, target, axes)
        delegate.onNestedScrollAccepted(child, target, axes, -1)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(target, dx, dy, consumed)
        delegate.onNestedPreScroll(target, dx, dy, consumed, type)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        super.onNestedPreScroll(target, dx, dy, consumed)
        delegate.onNestedPreScroll(target, dx, dy, consumed, -1)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        delegate.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        delegate.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        delegate.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, -1)
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return delegate.onNestedPreFling(target, velocityX, velocityY)
                || super.onNestedPreFling(target, velocityX, velocityY)
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return delegate.onNestedFling(target, velocityX, velocityY, consumed)
                || super.onNestedFling(target, velocityX, velocityY, consumed)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        super.onStopNestedScroll(target)
        delegate.onStopNestedScroll(target, type)
    }

    override fun onStopNestedScroll(target: View) {
        super.onStopNestedScroll(target)
        delegate.onStopNestedScroll(target, -1)
    }
}