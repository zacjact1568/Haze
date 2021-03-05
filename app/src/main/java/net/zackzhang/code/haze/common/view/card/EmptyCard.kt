package net.zackzhang.code.haze.common.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

class EmptyCard(parent: ViewGroup) : BaseCard(parent, R.layout.card_empty) {

    override fun updateViews(cardData: BaseCardData) {

    }
}