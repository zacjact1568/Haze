package me.imzack.app.cold.model.bean

/** 对应数据库中 basic 表 */
data class Basic(
        val cityId: String,
        val cityName: String,
        var updateTime: Long = 0L
)