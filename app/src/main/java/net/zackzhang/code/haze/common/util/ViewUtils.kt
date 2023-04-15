package net.zackzhang.code.haze.common.util

import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView

fun LottieAnimationView.resumeAnimationWhenPause() {
    if (!isVisible || isAnimating) return
    resumeAnimation()
}

fun LottieAnimationView.pauseAnimationWhenPlaying() {
    if (!isVisible || !isAnimating) return
    pauseAnimation()
}