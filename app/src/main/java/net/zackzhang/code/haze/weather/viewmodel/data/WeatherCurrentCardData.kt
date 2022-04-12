package net.zackzhang.code.haze.weather.viewmodel.data

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import net.zackzhang.code.haze.common.constant.CARD_TYPE_WEATHER_CURRENT
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

data class WeatherCurrentCardData(
    @DrawableRes val iconResId: Int,
    @ColorInt val iconColor: Int,
    val value: String?,
    val description: String,
    val iconScale: Float = 1F,
) : BaseCardData(CARD_TYPE_WEATHER_CURRENT, spanSize = 1)