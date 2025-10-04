package net.zackzhang.code.haze.weather.viewmodel.data

import androidx.annotation.DrawableRes
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.common.constant.CARD_TYPE_WEATHER_HOURLY

data class WeatherHourlyCardData(
    val time: String,
    @param:DrawableRes
    val conditionIconRes: Int,
    val temperature: String,
) : BaseCardData(CARD_TYPE_WEATHER_HOURLY)
