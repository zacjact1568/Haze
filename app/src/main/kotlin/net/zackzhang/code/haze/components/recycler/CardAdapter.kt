package net.zackzhang.code.haze.components.recycler

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.zackzhang.code.haze.components.recycler.cards.CardBase
import net.zackzhang.code.haze.components.recycler.cards.CardConfigBase
import net.zackzhang.code.haze.components.recycler.cards.CardType
import net.zackzhang.code.haze.components.recycler.cards.EmptyCard
import net.zackzhang.code.haze.components.recycler.cards.SourceCard
import net.zackzhang.code.haze.components.recycler.cards.SpaceCard
import net.zackzhang.code.haze.utils.Coordinate2D
import net.zackzhang.code.haze.utils.EndCoordinates2D
import net.zackzhang.code.haze.utils.ItemDecorationInset
import net.zackzhang.code.haze.utils.ItemDecorationRectInsets
import net.zackzhang.code.haze.utils.forEachCounted

class CardAdapter(
    private val onItemClick: ((position: Int) -> Unit)? = null,
    private val creator: (type: Int, parent: ViewGroup) -> CardBase?,
) : RecyclerView.Adapter<CardBase>() {

    var spanCount = 1

    val lastCardPosition get() = cardConfigs.lastIndex

    private val cardConfigs = mutableListOf<CardConfigBase>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardBase {
        val card = creator(viewType, parent) ?: when (viewType) {
            CardType.SOURCE.ordinal -> SourceCard(parent)
            CardType.SPACE.ordinal -> SpaceCard(parent)
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

    override fun onBindViewHolder(holder: CardBase, position: Int) {
        holder.updateViews(cardConfigs[position])
    }

    override fun getItemCount() = cardConfigs.size

    override fun getItemViewType(position: Int) = cardConfigs[position].type.ordinal

    @SuppressLint("NotifyDataSetChanged")
    fun setCardConfigs(configs: List<CardConfigBase>) {
        cardConfigs.clear()
        cardConfigs.addAll(configs)
        notifyDataSetChanged()
    }

    fun addCard(config: CardConfigBase, position: Int = itemCount) {
        cardConfigs.add(position, config)
        notifyItemInserted(position)
    }

    fun replaceCard(config: CardConfigBase, position: Int) {
        cardConfigs[position] = config
        notifyItemChanged(position)
    }

    fun getSpanSize(position: Int) = cardConfigs[position].spanSize ?: spanCount

    fun isDecorationExists(position: Int): ItemDecorationRectInsets {
        val insets = cardConfigs[position].decorationRectInsets
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

    fun needBackground(position: Int) = cardConfigs[position].needBackground

    fun isCardTypeMatch(position: Int, type: CardType) = getItemViewType(position) == type.ordinal

    /**
     * 假定除了末尾，卡片填满所有格子
     */
    private fun findEndCoordinates(position: Int): EndCoordinates2D {
        val c = Coordinate2D(column = spanCount)
        cardConfigs.forEachCounted(position) {
            c += it.spanSize ?: spanCount
        }
        val (x, y) = c
        c += getSpanSize(position) - 1
        return EndCoordinates2D(x, y, c.x, c.y)
    }
}