package net.zackzhang.code.haze.weather.viewmodel.data

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.common.constant.CARD_TYPE_WEATHER_CURRENT

data class WeatherCurrentCardData(
    @param:DrawableRes val iconResId: Int,
    @param:ColorInt val iconColor: Int,
    val value: String?,
    val description: String,
    val iconScale: Float = 1F,
) : BaseCardData(CARD_TYPE_WEATHER_CURRENT, spanSize = 1)