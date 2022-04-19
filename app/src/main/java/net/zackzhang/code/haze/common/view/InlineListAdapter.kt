package net.zackzhang.code.haze.common.view

import android.database.DataSetObservable
import android.database.DataSetObserver
import android.view.ViewGroup
import net.zackzhang.code.haze.base.view.card.InlineBaseCard
import net.zackzhang.code.haze.base.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.common.view.card.InlineEmptyCard

class InlineListAdapter(
    private val creator: (parent: ViewGroup) -> InlineBaseCard?,
) {

    val count get() = cardDataList.size

    private val observable = object : DataSetObservable() {

        override fun registerObserver(observer: DataSetObserver?) {
            if (mObservers.contains(observer)) return
            super.registerObserver(observer)
        }

        override fun unregisterObserver(observer: DataSetObserver?) {
            if (!mObservers.contains(observer)) return
            super.unregisterObserver(observer)
        }
    }

    private val cardDataList = mutableListOf<BaseCardData>()

    fun registerDataSetObserver(observer: DataSetObserver) {
        observable.registerObserver(observer)
    }

    fun unregisterDataSetObserver(observer: DataSetObserver) {
        observable.unregisterObserver(observer)
    }

    fun setCardData(cardData: List<BaseCardData>) {
        cardDataList.clear()
        cardDataList.addAll(cardData)
        observable.notifyChanged()
    }

    fun getCardView(position: Int, parent: ViewGroup) =
        (creator(parent) ?: InlineEmptyCard(parent)).run {
            updateViews(cardDataList[position])
            itemView
        }
}