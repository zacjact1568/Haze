package net.zackzhang.code.haze.common.view

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.zackzhang.code.haze.common.Constants
import net.zackzhang.code.haze.common.view.card.BaseCard
import net.zackzhang.code.haze.common.view.card.EmptyCard
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

class CardAdapter(private val creator: (type: Int, parent: ViewGroup) -> BaseCard?)
    : RecyclerView.Adapter<BaseCard>() {

    private val cardDataList = mutableListOf<BaseCardData>()

    private val columnMap = hashMapOf<Int, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        (creator(viewType, parent) ?: EmptyCard(parent)).apply {
            columnMap[viewType] = column
        }

    override fun onBindViewHolder(holder: BaseCard, position: Int) {
        holder.updateViews(cardDataList[position])
    }

    override fun getItemCount() = cardDataList.size

    override fun getItemViewType(position: Int) = cardDataList[position].type

    @SuppressLint("NotifyDataSetChanged")
    fun setCardData(cardData: List<BaseCardData>) {
        cardDataList.clear()
        cardDataList.addAll(cardData)
        notifyDataSetChanged()
    }

    fun getColumn(position: Int) = columnMap[getItemViewType(position)] ?: Constants.CARD_COLUMN_DEFAULT
}