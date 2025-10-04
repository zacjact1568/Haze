package net.zackzhang.code.haze.common.viewmodel.data

import androidx.annotation.Dimension
import net.zackzhang.code.haze.common.util.ItemDecorationRectInsets
import net.zackzhang.code.haze.common.constant.CARD_TYPE_SPACE

class SpaceCardData(
    @param:Dimension
    val horizontal: Int = 0,
    @param:Dimension
    val vertical: Int = 0,
) : BaseCardData(
    CARD_TYPE_SPACE,
    decorationRectInsets = ItemDecorationRectInsets(
        left = false,
        right = false,
        top = false,
        bottom = false,
    ),
)