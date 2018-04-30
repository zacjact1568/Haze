package me.imzack.app.cold.model.bean

data class Weather(
        val cityId: String,
        val cityName: String,
        val current: Current = Current(),
        val hourlyForecasts: Array<HourlyForecast> = Array(HOURLY_FORECAST_LENGTH) { HourlyForecast() },
        val dailyForecasts: Array<DailyForecast> = Array(DAILY_FORECAST_LENGTH) { DailyForecast() },
        var updateTime: Long = 0L,
        // 当前更新状态
        var status: Int = STATUS_GENERAL
) {

    companion object {

        // 每 3 小时，3 * 8 = 24
        const val HOURLY_FORECAST_LENGTH = 8
        const val DAILY_FORECAST_LENGTH = 7
        // 温度趋势图中只显示 5 个
        const val DAILY_FORECAST_LENGTH_DISPLAY = 5

        const val STATUS_ON_UPDATING = 1
        const val STATUS_GENERAL = 0
        const val STATUS_DELETED = -1
    }

    val isOnUpdating
        get() = status == STATUS_ON_UPDATING

    data class Current(
            var conditionCode: Int = 0,
            var temperature: Int = 0,
            var feelsLike: Int = 0,
            var airQualityIndex: Int = 0
    )

    data class HourlyForecast(
            var time: Long = 0L,
            var temperature: Int = 0,
            var precipitationProbability: Int = 0
    )

    data class DailyForecast(
            var date: Long = 0L,
            var temperatureMax: Int = 0,
            var temperatureMin: Int = 0,
            var conditionCodeDay: Int = 0,
            // 暂时不显示
            var conditionCodeNight: Int = 0,
            var precipitationProbability: Int = 0
    )
}
