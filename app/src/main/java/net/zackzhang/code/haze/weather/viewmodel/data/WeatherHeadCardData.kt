package net.zackzhang.code.haze.weather.viewmodel.data

import net.zackzhang.code.haze.common.Constants
import net.zackzhang.code.haze.common.model.entity.ThemeEntity
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

data class WeatherHeadCardData(
    val temperatureNow: Int?,
    val temperatureRange: IntRange?,
    val condition: String?,
    val airQuality: String?,
    val updatedAt: String?,
    val theme: ThemeEntity,
) : BaseCardData(Constants.CARD_TYPE_WEATHER_HEAD)