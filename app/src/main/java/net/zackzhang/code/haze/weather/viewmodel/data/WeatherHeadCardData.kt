package net.zackzhang.code.haze.weather.viewmodel.data

import net.zackzhang.code.haze.common.Constants
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

data class WeatherHeadCardData(
    var temperatureNow: String,
    var condition: String,
    var airQuality: String,
) : BaseCardData(Constants.CARD_TYPE_WEATHER_HEAD)