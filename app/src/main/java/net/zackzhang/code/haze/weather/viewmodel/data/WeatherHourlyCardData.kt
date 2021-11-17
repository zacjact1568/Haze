package net.zackzhang.code.haze.weather.viewmodel.data

import net.zackzhang.code.haze.common.constant.CARD_TYPE_WEATHER_HOURLY
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

data class WeatherHourlyCardData(
    val hourly: List<WeatherHourlyItemCardData>
) : BaseCardData(CARD_TYPE_WEATHER_HOURLY)