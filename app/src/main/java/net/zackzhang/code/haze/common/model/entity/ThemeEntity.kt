package net.zackzhang.code.haze.common.model.entity

import androidx.annotation.ColorInt

data class ThemeEntity(
    @ColorInt val backgroundColor: Int,
    @ColorInt val foregroundColor: Int,
)
