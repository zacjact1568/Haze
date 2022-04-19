package net.zackzhang.code.haze.base.view.card

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.zackzhang.code.haze.base.viewmodel.data.BaseCardData

abstract class BaseCard(
    parent: ViewGroup,
    resId: Int,
    onClick: ((position: Int) -> Unit)? = null,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(resId, parent, false)) {

    protected val context = parent.context!!

    init {
        onClick?.let {
            itemView.setOnClickListener { _ ->
                it(layoutPosition)
            }
        }
    }

    abstract fun updateViews(cardData: BaseCardData)
}