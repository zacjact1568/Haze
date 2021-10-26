package net.zackzhang.code.haze.city.viewmodel.data

import net.zackzhang.code.haze.common.constant.CARD_TYPE_CITY_SEARCH_ASSOCIATION
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

class CitySearchAssociationCardData(
    var name: String,
    var prefecture: String,
    var province: String
) : BaseCardData(CARD_TYPE_CITY_SEARCH_ASSOCIATION)