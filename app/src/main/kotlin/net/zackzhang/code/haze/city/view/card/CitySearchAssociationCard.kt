package net.zackzhang.code.haze.city.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.city.viewmodel.data.CitySearchAssociationCardData
import net.zackzhang.code.haze.common.util.orPlaceholder
import net.zackzhang.code.haze.common.view.card.BaseCard
import net.zackzhang.code.haze.databinding.CardCitySearchAssociationBinding
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

class CitySearchAssociationCard(parent: ViewGroup, private var selectedCallback: (Int) -> Unit) :
    BaseCard(parent, R.layout.card_city_search_association) {

    private val binding = CardCitySearchAssociationBinding.bind(itemView)

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is CitySearchAssociationCardData) return
        binding.run {
            vCityName.text = cardData.name.orPlaceholder()
            vCityPrefectureProvinceCountry.text = context.getString(
                R.string.city_search_association_prefecture_province_country_format,
                cardData.prefecture.orPlaceholder(),
                cardData.province.orPlaceholder(),
                cardData.country.orPlaceholder()
            )
            root.setOnClickListener {
                selectedCallback(layoutPosition)
            }
        }
    }
}