package net.zackzhang.code.haze.weather.model.entity

import net.zackzhang.code.haze.air.model.entity.AirEntity
import net.zackzhang.code.haze.air.model.entity.AirNowEntity
import net.zackzhang.code.haze.base.util.plusAssign
import net.zackzhang.code.haze.weather.util.getTemperatureRange
import kotlin.math.max
import kotlin.math.min

data class WeatherEntity(
    val now: WeatherHourlyEntity,
    val hourly: List<WeatherHourlyEntity>,
    val daily: List<WeatherDailyEntity>,
    val air: AirEntity?,
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

    val dbHourly: List<WeatherHourlyEntity>
        get() = mutableListOf(now).also { it += hourly }

    val dbDaily get() = daily

    val dbAir: List<AirNowEntity>
        get() = if (air == null) emptyList() else mutableListOf(air.now).also { it += air.stations }

    val todayTemperatureRange: IntRange?
        get() = if (daily.isEmpty()) null else daily[0].getTemperatureRange()

    val temperatureRangeAmongAllDates: IntRange? by lazy {
        var min = Int.MAX_VALUE
        var max = Int.MIN_VALUE
        daily.forEach {
            min = min(min, it.temperatureMin ?: min)
            max = max(max, it.temperatureMax ?: max)
        }
        min..max
    }

    val updatedAt = now.updatedAt

    fun attachCityId(cityId: String) {
        now.cityId = cityId
        hourly.forEach { it.cityId = cityId }
        daily.forEach { it.cityId = cityId }
        air?.attachCityId(cityId)
    }
}
