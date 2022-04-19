package net.zackzhang.code.haze.base.view.card

import android.view.LayoutInflater
import android.view.ViewGroup
import net.zackzhang.code.haze.base.viewmodel.data.BaseCardData

abstract class InlineBaseCard(parent: ViewGroup, resId: Int) {

    val itemView = LayoutInflater.from(parent.context).inflate(resId, parent, false)!!

    abstract fun updateViews(cardData: BaseCardData)
}