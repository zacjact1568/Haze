package net.zackzhang.code.haze.common.view

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.zackzhang.code.haze.base.util.*
import net.zackzhang.code.haze.base.view.card.BaseCard
import net.zackzhang.code.haze.base.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.common.constant.CARD_TYPE_SOURCE
import net.zackzhang.code.haze.common.constant.CARD_TYPE_SPACE
import net.zackzhang.code.haze.common.view.card.EmptyCard
import net.zackzhang.code.haze.common.view.card.SourceCard
import net.zackzhang.code.haze.common.view.card.SpaceCard

class CardAdapter(
    private val onItemClick: ((position: Int) -> Unit)? = null,
    private val creator: (type: Int, parent: ViewGroup) -> BaseCard?,
) : RecyclerView.Adapter<BaseCard>() {

    var spanCount = 1

    val lastCardPosition get() = cardDataList.lastIndex

    private val cardDataList = mutableListOf<BaseCardData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseCard {
        val card = creator(viewType, parent) ?: when (viewType) {
            CARD_TYPE_SOURCE -> SourceCard(parent)
            CARD_TYPE_SPACE -> SpaceCard(parent)
            // Other default cards
            else -> EmptyCard(parent)
        }
        onItemClick?.let {
            val v = card.itemView
            if (!v.hasOnClickListeners()) {
                v.setOnClickListener { _ ->
                    it(card.layoutPosition)
                }
            }
        }
        return card
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

    fun addCard(cardData: BaseCardData, position: Int = itemCount) {
        cardDataList.add(position, cardData)
        notifyItemInserted(position)
    }

    fun replaceCard(cardData: BaseCardData, position: Int) {
        cardDataList[position] = cardData
        notifyItemChanged(position)
    }

    fun getSpanSize(position: Int) = cardDataList[position].spanSize ?: spanCount

    fun isDecorationExists(position: Int): ItemDecorationRectInsets {
        val insets = cardDataList[position].decorationRectInsets
        val c = findEndCoordinates(position)
        return ItemDecorationRectInsets(
            if (insets?.left?.exist != false) {
                if (c.startX == 0) ItemDecorationInset.FULL else ItemDecorationInset.HALF
            } else ItemDecorationInset.NONE,
            if (insets?.right?.exist != false) {
                if (c.endX == spanCount - 1) ItemDecorationInset.FULL else ItemDecorationInset.HALF
            } else ItemDecorationInset.NONE,
            if (insets?.top?.exist != false && c.startY == 0)
                ItemDecorationInset.FULL else ItemDecorationInset.NONE,
            if (insets?.bottom?.exist != false)
                ItemDecorationInset.FULL else ItemDecorationInset.NONE
        )
    }

    fun needBackground(position: Int) = cardDataList[position].needBackground

    fun isCardTypeMatch(position: Int, type: Int) = getItemViewType(position) == type

    /**
     * 假定除了末尾，卡片填满所有格子
     */
    private fun findEndCoordinates(position: Int): EndCoordinates2D {
        val c = Coordinate2D(column = spanCount)
        cardDataList.forEachCounted(position) {
            c += it.spanSize ?: spanCount
        }
        val (x, y) = c
        c += getSpanSize(position) - 1
        return EndCoordinates2D(x, y, c.x, c.y)
    }
}