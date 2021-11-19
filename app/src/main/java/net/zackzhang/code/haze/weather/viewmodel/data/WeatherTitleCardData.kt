package net.zackzhang.code.haze.weather.viewmodel.data

import net.zackzhang.code.haze.common.constant.CARD_TYPE_WEATHER_TITLE
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

class WeatherTitleCardData(
    val title: String,
) : BaseCardData(CARD_TYPE_WEATHER_TITLE)