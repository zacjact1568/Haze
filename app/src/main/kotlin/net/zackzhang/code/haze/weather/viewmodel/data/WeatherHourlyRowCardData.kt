package net.zackzhang.code.haze.weather.viewmodel.data

import net.zackzhang.code.haze.common.util.ItemDecorationRectInsets
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.common.constant.CARD_TYPE_WEATHER_HOURLY_ROW

data class WeatherHourlyRowCardData(
    val hourly: List<WeatherHourlyCardData>
) : BaseCardData(
    CARD_TYPE_WEATHER_HOURLY_ROW,
    decorationRectInsets = ItemDecorationRectInsets(
        left = false,
        right = false,
        top = true,
        bottom = true
    ),
)