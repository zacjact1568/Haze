package net.zackzhang.code.haze.weather.viewmodel.data

import androidx.annotation.DrawableRes
import net.zackzhang.code.haze.common.constant.CARD_TYPE_WEATHER_HOURLY_ITEM
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

data class WeatherHourlyItemCardData(
    val time: String,
    @DrawableRes
    val conditionIconRes: Int,
    val temperature: String,
) : BaseCardData(CARD_TYPE_WEATHER_HOURLY_ITEM)
