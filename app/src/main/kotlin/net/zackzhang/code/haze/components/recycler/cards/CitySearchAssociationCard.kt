package net.zackzhang.code.haze.components.recycler.cards

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.databinding.CardCitySearchAssociationBinding
import net.zackzhang.code.haze.utils.orPlaceholder

class CitySearchAssociationCard(
    parent: ViewGroup,
    private var selectedCallback: (Int) -> Unit,
) : CardBase(parent, R.layout.card_city_search_association) {

    private val binding = CardCitySearchAssociationBinding.bind(itemView)

    override fun updateViews(config: CardConfigBase) {
        if (config !is CitySearchAssociationCardConfig) return
        binding.run {
            vCityName.text = config.name.orPlaceholder()
            vCityPrefectureProvinceCountry.text = context.getString(
                R.string.city_search_association_prefecture_province_country_format,
                config.prefecture.orPlaceholder(),
                config.province.orPlaceholder(),
                config.country.orPlaceholder()
            )
            root.setOnClickListener {
                selectedCallback(layoutPosition)
            }
        }
    }
}

class CitySearchAssociationCardConfig(
    val name: String?,
    val prefecture: String?,
    val province: String?,
    val country: String?,
) : CardConfigBase(CardType.CITY_SEARCH_ASSOCIATION)