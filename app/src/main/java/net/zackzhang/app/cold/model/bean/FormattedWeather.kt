package net.zackzhang.app.cold.model.bean

import net.zackzhang.app.cold.R
import net.zackzhang.app.cold.common.Constant
import net.zackzhang.app.cold.util.ResourceUtil

data class FormattedWeather(
        var isUpdating: Boolean,
        var cityName: String,
        var condition: String = Constant.UNKNOWN_DATA,
        var temperature: String = Constant.UNKNOWN_DATA,
        var updateTime: String = ResourceUtil.getString(R.string.text_invalid_data),
        var feelsLike: String = Constant.UNKNOWN_DATA,
        var tempRange: String = "${Constant.UNKNOWN_DATA} | ${Constant.UNKNOWN_DATA}",
        var airQuality: String = Constant.UNKNOWN_DATA,
        var weeks: Array<String> = Array(Weather.DAILY_FORECAST_LENGTH_DISPLAY) {""},
        var conditions: Array<String> = Array(Weather.DAILY_FORECAST_LENGTH_DISPLAY) {""},
        var maxTemps: IntArray = IntArray(Weather.DAILY_FORECAST_LENGTH_DISPLAY),
        var minTemps: IntArray = IntArray(Weather.DAILY_FORECAST_LENGTH_DISPLAY)
)
