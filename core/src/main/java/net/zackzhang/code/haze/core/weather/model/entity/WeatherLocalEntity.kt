package net.zackzhang.code.haze.core.weather.model.entity

import androidx.room.Embedded
import androidx.room.Relation
import net.zackzhang.code.haze.core.air.model.entity.AirNowEntity
import net.zackzhang.code.haze.core.city.model.entity.CityWeatherEntity
import net.zackzhang.code.haze.core.common.constant.CITY_ID
import net.zackzhang.code.haze.core.common.constant.ID

data class WeatherLocalEntity(
    @Embedded
    val city: CityWeatherEntity,
    @Relation(
        parentColumn = ID,
        entityColumn = CITY_ID,
    )
    val hourlyList: List<WeatherHourlyEntity>,
    @Relation(
        parentColumn = ID,
        entityColumn = CITY_ID,
    )
    val dailyList: List<WeatherDailyEntity>,
    @Relation(
        parentColumn = ID,
        entityColumn = CITY_ID,
    )
    val airList: List<AirNowEntity>,
)