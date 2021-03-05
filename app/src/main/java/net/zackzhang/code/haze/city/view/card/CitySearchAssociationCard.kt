package net.zackzhang.code.haze.city.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.city.viewmodel.data.CitySearchAssociationCardData
import net.zackzhang.code.haze.common.view.card.BaseCard
import net.zackzhang.code.haze.databinding.CardCitySearchAssociationBinding
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

class CitySearchAssociationCard(parent: ViewGroup, private var selectedCallback: (Int) -> Unit) :
    BaseCard(parent, R.layout.card_city_search_association) {

    private val binding = CardCitySearchAssociationBinding.bind(itemView)

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is CitySearchAssociationCardData) return
        binding.run {
            vCityName.text = cardData.name
            vCityPrefectureProvince.text = context.getString(R.string.format_city_search_association_prefecture_province, cardData.prefecture, cardData.province)
            root.setOnClickListener {
                selectedCallback(layoutPosition)
            }
        }
    }
}