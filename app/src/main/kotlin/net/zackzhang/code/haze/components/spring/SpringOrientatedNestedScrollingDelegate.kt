package net.zackzhang.code.haze.components.spring

import androidx.core.view.ViewCompat
import kotlin.math.abs

class SpringOrientatedNestedScrollingDelegate(
    private val behavior: SpringNestedScrollingBehavior,
) {

    companion object {

        const val DAMPING_COEFFICIENT = 0.01F
    }

    fun onScrollChanged(old: Int, new: Int) {
        // log(this::class, "onScrollChanged", "$old -> $new")
    }

    fun onStartNestedScroll(type: Int): Boolean {
        // log(this::class, "onStartNestedScroll", "Called")
        return true
    }

    fun onNestedScrollAccepted(type: Int) {
        // log(this::class, "onNestedScrollAccepted", "Called")
    }

    fun onNestedPreScroll(differential: Int, type: Int): Int {
        var consumed = 0
        val dd = damping(differential)
        val scroll = behavior.scrolledPosition
        /*
        log(
            this::class,
            "onNestedPreScroll",
            "differential = $differential, " +
                    "type = ${logType(type)}, " +
                    "dd = $dd, " +
                    "scroll = $scroll",
        )
        */
        // child 可滚动时，scroll 一定为 0，此时 parent 不嵌套滚动
        // 但有特殊情况，child 滚动到顶部或底部时，手指再向下或上滑动，parent 应该开始嵌套滚动并消耗 differential
        // 但这种情况下，根据 differential 只能区分手指向下或上滑动，无法区分 child 滚动到顶部还是底部
        // child 是 RecyclerView 的话，其 scroll 始终为 0，虽然可以通过它单独的方法获得滚动位置，但还得判断类型
        // 因此这种临界情况，parent 一律不消耗 differential，稍后在 onNestedScroll 中判断
        if (type == ViewCompat.TYPE_TOUCH && scroll != 0) {
            if (reversal(scroll, scroll + dd)) {
                // 复位，否则 parent 再滚动 dd 就越界了，应只滚动至 0，剩余未消耗的 differential 交由 child 处理
                consumed = -scroll
                behavior.scrollTo(0)
            } else {
                // 如果这里消耗完了，onNestedScroll 就不会被调用了
                consumed = differential
                behavior.scrollBy(dd)
            }
        }
        return consumed
    }

    fun onNestedScroll(consumed: Int, unconsumed: Int, type: Int) {
        val dd = damping(unconsumed)
        val scroll = behavior.scrolledPosition
        /*
        log(
            this::class,
            "onNestedScroll",
            "consumed = $consumed, " +
                    "unconsumed = $unconsumed, " +
                    "type = ${logType(type)}, " +
                    "dd = $dd, " +
                    "scroll = $scroll",
        )
        */
        // 临界情况判断，前提是 scroll == 0
        // 如果是 consumed != 0 && unconsumed == 0，则 child 处于可滚动状态并已经滚动 consumed，parent 不嵌套滚动
        // 如果是 consumed == 0 && unconsumed != 0，则 child 处于不可滚动状态，parent 嵌套滚动并消耗 unconsumed
        // 只要让 parent 开始嵌套滚动，scroll 就不为 0 了，onNestedPreScroll 就会消耗滑动距离 differential，就不会再执行到这里了
        if (type == ViewCompat.TYPE_TOUCH && scroll == 0 && consumed == 0 && unconsumed != 0) {
            behavior.scrollBy(dd)
        } else if (type == ViewCompat.TYPE_NON_TOUCH && scroll == 0 && consumed != 0 && unconsumed == 0) {
            // child fling 到头，还有未消耗的 fling 距离 unconsumed
            // TODO fling：初始速度 & unconsumed
            behavior.flingBack()
        }
    }

    fun onNestedPreFling(velocity: Float): Boolean {
        /*
        log(
            this::class,
            "onNestedPreFling",
            "scroll = ${behavior.scrolledPosition}, " +
                    "velocity = $velocity",
        )
        */
        return false
    }

    fun onNestedFling(velocity: Float, consumed: Boolean): Boolean {
        /*
        log(
            this::class,
            "onNestedFling",
            "scroll = ${behavior.scrolledPosition}, " +
                    "velocity = $velocity, " +
                    "consumed = $consumed",
        )
        */
        return false
    }

    fun onStopNestedScroll(type: Int) {
        val scroll = behavior.scrolledPosition
        /*
        log(
            this::class,
            "onStopNestedScroll",
            "type = ${logType(type)}, " +
                    "scroll = $scroll\n---------------------",
        )
        */
        if (type == ViewCompat.TYPE_TOUCH && scroll != 0) {
            // parent 嵌套滚动到原始位置
            behavior.scrollBack()
        }
    }

    /**
     * 滚动距离越大，衰减比率越大
     * [DAMPING_COEFFICIENT] 控制分段，i.e.
     * scrollValue 的绝对值在 0~200，滚动跟手
     * scrollValue 的绝对值在 200~300，ratio 为 2，滚动减缓 1 倍，以此类推
     */
    private fun damping(differential: Int): Int {
        val ratio = (abs(behavior.scrolledPosition) * DAMPING_COEFFICIENT).toInt()
        return if (ratio == 0) differential else differential / ratio
    }

    private fun reversal(before: Int, after: Int) = before > 0 && after < 0 || before < 0 && after > 0

    private fun logType(type: Int) = when (type) {
        ViewCompat.TYPE_TOUCH -> "TOUCH"
        ViewCompat.TYPE_NON_TOUCH -> "NON_TOUCH"
        else -> "UNKNOWN"
    }
}