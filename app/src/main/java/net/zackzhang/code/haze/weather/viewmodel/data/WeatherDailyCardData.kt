package net.zackzhang.code.haze.weather.viewmodel.data

import androidx.annotation.DrawableRes
import net.zackzhang.code.haze.common.constant.CARD_TYPE_WEATHER_DAILY
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

class WeatherDailyCardData(
    val date: String,
    @DrawableRes
    val conditionIconRes: Int,
    val temperatureNow: Int?,
    val temperatureRange: IntRange?,
    val temperatureRangeAmongAllDates: IntRange?,
) : BaseCardData(CARD_TYPE_WEATHER_DAILY)