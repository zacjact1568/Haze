package me.imzack.app.cold.model.bean

data class City(
        val id: String,
        val name: String,
        val longitude: Double,
        val latitude: Double,
        // 地和省不是必需的
        val prefecture: String? = null,
        val province: String? = null
)
