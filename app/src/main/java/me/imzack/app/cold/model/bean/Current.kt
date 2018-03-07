package me.imzack.app.cold.model.bean

/** 对应数据库中 current 表 */
data class Current(
        val cityId: String,
        var conditionCode: Int = 0,
        var temperature: Int = 0,
        var feelsLike: Int = 0,
        var airQualityIndex: Int = 0
)