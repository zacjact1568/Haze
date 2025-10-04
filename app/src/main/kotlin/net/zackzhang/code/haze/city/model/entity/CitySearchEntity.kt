package net.zackzhang.code.haze.city.model.entity

data class CitySearchEntity(
    val input: String,
    val result: List<CityEntity>,
)