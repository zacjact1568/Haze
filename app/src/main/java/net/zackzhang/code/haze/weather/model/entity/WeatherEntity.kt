package net.zackzhang.code.haze.weather.model.entity

import net.zackzhang.code.haze.air.model.entity.AirEntity
import net.zackzhang.code.haze.air.model.entity.AirNowEntity

data class WeatherEntity(
    val now: WeatherHourlyEntity,
    val hourly: List<WeatherHourlyEntity>,
    val daily: List<WeatherDailyEntity>,
    val air: AirEntity,
) {

    companion object {

        fun fromWeatherLocalEntity(entity: WeatherLocalEntity) = WeatherEntity(
            entity.hourlyList.first { it.isNow },
            entity.hourlyList.filterNot { it.isNow },
            entity.dailyList,
            AirEntity.fromAirNowEntityList(entity.airList)
        )
    }

    val cityId get() = now.cityId

    val dbHourly get() = mutableListOf(now).also { it += hourly } as List<WeatherHourlyEntity>

    val dbDaily get() = daily

    val dbAir get() = mutableListOf(air.now).also { it += air.stations } as List<AirNowEntity>

    val todayTemperatureRange: IntRange? get() {
        if (daily.isEmpty()) {
            return null
        }
        val today = daily[0]
        if (today.temperatureMin == null || today.temperatureMax == null) {
            return null
        }
        return today.temperatureMin..today.temperatureMax
    }

    fun attachCityId(cityId: String) {
        now.cityId = cityId
        hourly.forEach { it.cityId = cityId }
        daily.forEach { it.cityId = cityId }
        air.attachCityId(cityId)
    }
}
