package net.zackzhang.code.haze.weather.viewmodel.data

import androidx.annotation.DrawableRes
import net.zackzhang.code.haze.common.constant.CARD_TYPE_WEATHER_CURRENT
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

data class WeatherCurrentCardData(
    @DrawableRes val iconResId: Int,
    val value: String?,
    val description: String,
) : BaseCardData(CARD_TYPE_WEATHER_CURRENT, 1)