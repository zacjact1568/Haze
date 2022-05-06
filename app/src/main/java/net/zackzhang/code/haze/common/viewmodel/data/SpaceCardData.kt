package net.zackzhang.code.haze.common.viewmodel.data

import androidx.annotation.Dimension
import net.zackzhang.code.haze.base.util.ItemDecorationRectInsets
import net.zackzhang.code.haze.base.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.common.constant.CARD_TYPE_SPACE

class SpaceCardData(
    @Dimension
    val horizontal: Int = 0,
    @Dimension
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