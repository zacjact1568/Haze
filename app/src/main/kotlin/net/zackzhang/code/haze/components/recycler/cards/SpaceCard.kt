package net.zackzhang.code.haze.components.recycler.cards

import android.view.ViewGroup
import androidx.annotation.Dimension
import androidx.core.view.updateLayoutParams
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.databinding.CardSpaceBinding
import net.zackzhang.code.haze.utils.ItemDecorationRectInsets

open class SpaceCard(parent: ViewGroup) : CardBase(parent, R.layout.card_space) {

    private val binding = CardSpaceBinding.bind(itemView)

    override fun updateViews(config: CardConfigBase) {
        if (config !is SpaceCardConfig) return
        binding.root.updateLayoutParams {
            val useHorizontal = if (config.horizontal > 0 && config.vertical > 0) {
                config.horizontal > config.vertical
            } else {
                config.horizontal > 0
            }
            width = if (useHorizontal) config.horizontal else ViewGroup.LayoutParams.MATCH_PARENT
            height = if (useHorizontal) ViewGroup.LayoutParams.MATCH_PARENT else config.vertical
        }
    }
}

class EmptyCard(parent: ViewGroup) : SpaceCard(parent) {

    override fun updateViews(config: CardConfigBase) {

    }
}

class SpaceCardConfig(
    @param:Dimension
    val horizontal: Int = 0,
    @param:Dimension
    val vertical: Int = 0,
) : CardConfigBase(
    CardType.SPACE,
    decorationRectInsets = ItemDecorationRectInsets(
        left = false,
        right = false,
        top = false,
        bottom = false,
    ),
)