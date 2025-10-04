package net.zackzhang.code.haze.common.view.card

import android.view.LayoutInflater
import android.view.ViewGroup
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

abstract class InlineBaseCard(parent: ViewGroup, resId: Int) {

    val itemView = LayoutInflater.from(parent.context).inflate(resId, parent, false)!!

    abstract fun updateViews(cardData: BaseCardData)
}