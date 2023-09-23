package net.zackzhang.code.haze.common.view.card

import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.common.viewmodel.data.SpaceCardData
import net.zackzhang.code.haze.databinding.CardSpaceBinding

open class SpaceCard(parent: ViewGroup) : BaseCard(parent, R.layout.card_space) {

    private val binding = CardSpaceBinding.bind(itemView)

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is SpaceCardData) return
        binding.root.updateLayoutParams {
            val useHorizontal = if (cardData.horizontal > 0 && cardData.vertical > 0) {
                cardData.horizontal > cardData.vertical
            } else {
                cardData.horizontal > 0
            }
            width = if (useHorizontal) cardData.horizontal else ViewGroup.LayoutParams.MATCH_PARENT
            height = if (useHorizontal) ViewGroup.LayoutParams.MATCH_PARENT else cardData.vertical
        }
    }
}