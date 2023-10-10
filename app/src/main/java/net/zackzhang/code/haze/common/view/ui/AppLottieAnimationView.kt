package net.zackzhang.code.haze.common.view.ui

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView

class AppLottieAnimationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LottieAnimationView(context, attrs) {

    private enum class StatusOnCompositionLoaded {
        PLAY, CANCEL, RESUME, PAUSE,
    }

    private val compositionLoaded
        get() = composition != null

    private var statusOnCompositionLoaded: StatusOnCompositionLoaded? = null

    init {
        // 对 ColorFilter 的设置会在构造阶段包装到 LottieValueCallback 里面
        // 如果 Composition 还未加载完成，则会缓存在 LottieDrawable#lazyCompositionTasks 中
        // 等到 LottieDrawable#setComposition 调用，即 Composition 加载完成之后才取出执行
        // 但调用 cancel/pauseAnimation 会将 lazyCompositionTasks 清空
        // 故如果在 Composition 加载完成之前调用 cancel/pauseAnimation 会导致 ColorFilter 失效
        // 解决方案是将此前所有对动画的操作延后到 Composition 加载完成时
        addLottieOnCompositionLoadedListener {
            when (statusOnCompositionLoaded) {
                StatusOnCompositionLoaded.PLAY -> super.playAnimation()
                StatusOnCompositionLoaded.CANCEL -> super.cancelAnimation()
                StatusOnCompositionLoaded.RESUME -> super.resumeAnimation()
                StatusOnCompositionLoaded.PAUSE -> super.pauseAnimation()
                else -> {}
            }
            statusOnCompositionLoaded = null
        }
    }

    override fun playAnimation() {
        if (compositionLoaded) {
            super.playAnimation()
        } else {
            statusOnCompositionLoaded = StatusOnCompositionLoaded.PLAY
        }
    }

    override fun cancelAnimation() {
        if (compositionLoaded) {
            super.cancelAnimation()
        } else {
            statusOnCompositionLoaded = StatusOnCompositionLoaded.CANCEL
        }
    }

    override fun resumeAnimation() {
        if (compositionLoaded) {
            super.resumeAnimation()
        } else {
            statusOnCompositionLoaded = StatusOnCompositionLoaded.RESUME
        }
    }

    override fun pauseAnimation() {
        if (compositionLoaded) {
            super.pauseAnimation()
        } else {
            statusOnCompositionLoaded = StatusOnCompositionLoaded.PAUSE
        }
    }

    fun playAnimationAndShow() {
        isVisible = true
        playAnimation()
    }

    fun cancelAnimationAndHide() {
        cancelAnimation()
        isInvisible = true
    }

    fun cancelAnimationAndGone() {
        cancelAnimation()
        isVisible = false
    }

    fun resumeAnimationIfVisible() {
        if (isVisible) {
            resumeAnimation()
        }
    }

    fun pauseAnimationIfVisible() {
        if (isVisible) {
            pauseAnimation()
        }
    }
}