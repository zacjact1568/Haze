package net.zackzhang.code.haze.common.view

import androidx.core.view.WindowInsetsCompat

data class SystemBarInsets(
    val status: Int,
    val navigation: Int,
) {

    companion object {

        fun fromWindowInsets(insets: WindowInsetsCompat) = SystemBarInsets(
            insets.getInsets(WindowInsetsCompat.Type.statusBars()).top,
            insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
        )
    }
}