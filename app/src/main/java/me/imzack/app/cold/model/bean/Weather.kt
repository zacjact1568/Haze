package me.imzack.app.cold.model.bean

data class Weather(
        val city: City,
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

        /** 状态：正在更新 */
        const val STATUS_UPDATING = 1
        /** 状态：未在更新 */
        const val STATUS_GENERAL = 0
        /** 状态：已删除 */
        const val STATUS_DELETED = -1
    }

    val cityId
        get() = city.id

    val cityName
        get() = city.name

    val isPrefecture
        get() = city.isPrefecture

    val isNewAdded
        get() = updateTime == 0L

    // TODO 若上一次刷新时间过久，自动刷新；若上一次刷新时间过近，不执行刷新
    // val isUpdatedLongAgo

    val isUpdating
        get() = status == STATUS_UPDATING

    data class City(
            var id: String = "",
            var name: String = "",
            var isPrefecture: Boolean = false
    )

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
