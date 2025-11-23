package net.zackzhang.code.haze.components.inline

import android.view.LayoutInflater
import android.view.ViewGroup
import net.zackzhang.code.haze.components.recycler.cards.CardConfigBase

abstract class InlineCardBase(parent: ViewGroup, resId: Int) {

    val itemView = LayoutInflater.from(parent.context).inflate(resId, parent, false)!!

    abstract fun updateViews(config: CardConfigBase)
}