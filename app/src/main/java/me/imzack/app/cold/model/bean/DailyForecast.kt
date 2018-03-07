package me.imzack.app.cold.model.bean

/** 对应数据库中 daily_forecast 表 */
data class DailyForecast(
        val cityId: String,
        val sequence: Int,
        var date: Long = 0L,
        var temperatureMax: Int = 0,
        var temperatureMin: Int = 0,
        var conditionCodeDay: Int = 0,
        // 暂时不显示
        var conditionCodeNight: Int = 0,
        var precipitationProbability: Int = 0
)