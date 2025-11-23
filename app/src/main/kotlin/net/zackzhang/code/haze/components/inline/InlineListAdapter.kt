package net.zackzhang.code.haze.components.inline

import android.database.DataSetObservable
import android.database.DataSetObserver
import android.view.ViewGroup
import net.zackzhang.code.haze.components.recycler.cards.CardConfigBase

class InlineListAdapter(
    private val creator: (parent: ViewGroup) -> InlineCardBase?,
) {

    val count get() = cardConfigs.size

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

    private val cardConfigs = mutableListOf<CardConfigBase>()

    fun registerDataSetObserver(observer: DataSetObserver) {
        observable.registerObserver(observer)
    }

    fun unregisterDataSetObserver(observer: DataSetObserver) {
        observable.unregisterObserver(observer)
    }

    fun setCardConfigs(configs: List<CardConfigBase>) {
        cardConfigs.clear()
        cardConfigs.addAll(configs)
        observable.notifyChanged()
    }

    fun getCardView(position: Int, parent: ViewGroup) =
        (creator(parent) ?: InlineEmptyCard(parent)).run {
            updateViews(cardConfigs[position])
            itemView
        }
}