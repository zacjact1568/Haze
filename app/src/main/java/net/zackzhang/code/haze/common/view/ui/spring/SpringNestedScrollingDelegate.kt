package net.zackzhang.code.haze.common.view.ui.spring

import android.view.View
import android.widget.OverScroller
import androidx.core.view.ViewCompat
import net.zackzhang.code.haze.base.util.Orientation
import net.zackzhang.code.haze.base.util.switch

class SpringNestedScrollingDelegate(
    private val master: View,
    private val orientation: Orientation,
): SpringNestedScrollingBehavior {

    private val scroller = OverScroller(master.context)

    private val delegate = SpringOrientatedNestedScrollingDelegate(this)

    override val scrolledPosition
        get() = orientation.switch({ master.scrollX }, { master.scrollY })

    override fun scrollTo(position: Int) {
        orientation.switch({
            master.scrollTo(position, 0)
        }, {
            master.scrollTo(0, position)
        })
    }

    override fun scrollBy(distance: Int) {
        orientation.switch({
            master.scrollBy(distance, 0)
        }, {
            master.scrollBy(0, distance)
        })
    }

    override fun scrollBack() {
        val x = master.scrollX
        val y = master.scrollY
        scroller.startScroll(x, y, -x, -y)
        master.postInvalidateOnAnimation()
    }

    override fun flingBack() {
//        scroller.fling(0, 0, 0, ?, 0, 0, 0, 0, 0, d)
//        parent.postInvalidateOnAnimation()
    }

    fun computeScroll() {
        if (!scroller.computeScrollOffset()) return
        master.scrollTo(scroller.currX, scroller.currY)
        master.postInvalidateOnAnimation()
    }

    fun onScrollChanged(oldX: Int, oldY: Int, newX: Int, newY: Int) {
        orientation.switch({
            delegate.onScrollChanged(oldX, newX)
        }, {
            delegate.onScrollChanged(oldY, newY)
        })
    }

    fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return master.isEnabled && consistent(axes) && delegate.onStartNestedScroll(type)
    }

    fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        if (consistent(axes)) {
            delegate.onNestedScrollAccepted(type)
        }
    }

    fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        orientation.switch({
            consumed[0] = delegate.onNestedPreScroll(dx, type)
        }, {
            consumed[1] = delegate.onNestedPreScroll(dy, type)
        })
    }

    fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        orientation.switch({
            delegate.onNestedScroll(dxConsumed, dxUnconsumed, type)
        }, {
            delegate.onNestedScroll(dyConsumed, dyUnconsumed, type)
        })
    }

    fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return orientation.switch({
            delegate.onNestedPreFling(velocityX)
        }, {
            delegate.onNestedPreFling(velocityY)
        })
    }

    fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return orientation.switch({
            delegate.onNestedFling(velocityX, consumed)
        }, {
            delegate.onNestedFling(velocityY, consumed)
        })
    }

    fun onStopNestedScroll(target: View, type: Int) {
        delegate.onStopNestedScroll(type)
    }

    private fun consistent(axes: Int) = orientation.switch({
        axes and ViewCompat.SCROLL_AXIS_HORIZONTAL != 0
    }, {
        axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    })
}