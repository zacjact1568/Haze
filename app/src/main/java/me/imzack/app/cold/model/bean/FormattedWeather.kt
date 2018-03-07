package me.imzack.app.cold.model.bean

import me.imzack.app.cold.R
import me.imzack.app.cold.common.Constant
import me.imzack.app.cold.util.ResourceUtil

data class FormattedWeather(
        var cityName: String,
        var condition: String = Constant.UNKNOWN_DATA,
        var temperature: String = Constant.UNKNOWN_DATA,
        var updateTime: String = ResourceUtil.getString(R.string.text_invalid_data),
        var feelsLike: String = Constant.UNKNOWN_DATA,
        var tempRange: String = "${Constant.UNKNOWN_DATA} | ${Constant.UNKNOWN_DATA}",
        var airQuality: String = Constant.UNKNOWN_DATA,
        var weeks: Array<String> = trick(),
        var conditions: Array<String> = trick(),
        var maxTemps: IntArray = IntArray(Weather.DAILY_FORECAST_LENGTH_DISPLAY),
        var minTemps: IntArray = IntArray(Weather.DAILY_FORECAST_LENGTH_DISPLAY)
) {
    companion object {
        // 如果直接在构造函数中使用以下函数体，安装到手机时会报 DexArchiveBuilderException 的错误，可能是 Lambda 表达式的问题
        private fun trick() = Array(Weather.DAILY_FORECAST_LENGTH_DISPLAY) {""}
    }
}
