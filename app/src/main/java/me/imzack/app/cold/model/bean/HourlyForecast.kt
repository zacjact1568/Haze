package me.imzack.app.cold.model.bean

/** 对应数据库中 hourly_forecast 表 */
data class HourlyForecast(
        val cityId: String,
        val sequence: Int,
        var time: Long = 0L,
        var temperature: Int = 0,
        var precipitationProbability: Int = 0
)