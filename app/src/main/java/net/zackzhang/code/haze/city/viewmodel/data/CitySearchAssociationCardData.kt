package net.zackzhang.code.haze.city.viewmodel.data

import net.zackzhang.code.haze.base.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.common.constant.CARD_TYPE_CITY_SEARCH_ASSOCIATION

class CitySearchAssociationCardData(
    val name: String?,
    val prefecture: String?,
    val province: String?,
    val country: String?,
) : BaseCardData(CARD_TYPE_CITY_SEARCH_ASSOCIATION)