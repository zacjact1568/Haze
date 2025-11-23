package net.zackzhang.code.haze.components.recycler.cards

import android.view.ViewGroup
import net.zackzhang.code.haze.R

class SourceCard(parent: ViewGroup) : CardBase(parent, R.layout.card_source) {

    override fun updateViews(config: CardConfigBase) {

    }
}

class SourceCardConfig : CardConfigBase(CardType.SOURCE)