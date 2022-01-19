package net.zackzhang.code.haze.weather.viewmodel.data

import androidx.annotation.DrawableRes
import net.zackzhang.code.haze.common.constant.CARD_TYPE_WEATHER_HOURLY
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

data class WeatherHourlyCardData(
    val time: String,
    @DrawableRes
    val conditionIconRes: Int,
    val temperature: String,
) : BaseCardData(CARD_TYPE_WEATHER_HOURLY)
