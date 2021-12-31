package net.zackzhang.code.haze.common.viewmodel.data

import net.zackzhang.code.haze.common.util.ItemDecorationRectInsets

abstract class BaseCardData(
    val type: Int,
    /**
     * 一个卡片的网格跨度
     */
    val spanSize: Int? = null,
    val decorationRectInsets: ItemDecorationRectInsets? = null,
    val needBackground: Boolean = true,
)