package net.zackzhang.code.haze.common.view

import androidx.core.view.WindowInsetsCompat

data class SystemBarInsets(
    val status: Int,
    val navigation: Int,
) {

    companion object {

        fun fromWindowInsets(insets: WindowInsetsCompat) =
            // 不要用 WindowInsetsCompat.Type.statusBars()
            // Freeform Window 模式的 top 是 0
            insets.getInsets(WindowInsetsCompat.Type.systemBars()).run {
                SystemBarInsets(top, bottom)
            }
    }
}