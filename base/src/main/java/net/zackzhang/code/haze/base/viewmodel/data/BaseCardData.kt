package net.zackzhang.code.haze.base.viewmodel.data

import net.zackzhang.code.haze.base.view.ThemeEntity
import net.zackzhang.code.haze.base.util.ItemDecorationRectInsets

abstract class BaseCardData(
    val type: Int,
    var theme: ThemeEntity? = null,
    /**
     * 一个卡片的网格跨度
     */
    val spanSize: Int? = null,
    val decorationRectInsets: ItemDecorationRectInsets? = null,
    val needBackground: Boolean = true,
)