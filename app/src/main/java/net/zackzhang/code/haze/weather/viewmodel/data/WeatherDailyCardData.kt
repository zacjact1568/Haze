package net.zackzhang.code.haze.weather.viewmodel.data

import net.zackzhang.code.haze.common.constant.CARD_TYPE_WEATHER_DAILY
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

class WeatherDailyCardData(
    val daily: List<WeatherDailyItemCardData>
) : BaseCardData(CARD_TYPE_WEATHER_DAILY)