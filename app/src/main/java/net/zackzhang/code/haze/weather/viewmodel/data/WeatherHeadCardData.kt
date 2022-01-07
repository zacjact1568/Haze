package net.zackzhang.code.haze.weather.viewmodel.data

import net.zackzhang.code.haze.common.constant.CARD_TYPE_WEATHER_HEAD
import net.zackzhang.code.haze.common.model.entity.ThemeEntity
import net.zackzhang.code.haze.common.util.ItemDecorationRectInsets
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

class WeatherHeadCardData(
    val temperatureNow: Int?,
    val temperatureRange: IntRange?,
    val condition: String?,
    val airQuality: String?,
    val updatedAt: String?,
    theme: ThemeEntity,
) : BaseCardData(
    CARD_TYPE_WEATHER_HEAD,
    theme,
    decorationRectInsets = ItemDecorationRectInsets(
        left = false,
        right = false,
        top = false,
        bottom = true
    ),
    needBackground = false,
)