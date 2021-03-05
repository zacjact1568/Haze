package net.zackzhang.code.haze.weather.model.entity

import androidx.room.Embedded
import androidx.room.Relation
import net.zackzhang.code.haze.air.model.entity.AirNowEntity
import net.zackzhang.code.haze.city.model.entity.CityWeatherEntity
import net.zackzhang.code.haze.common.Constants
import net.zackzhang.code.haze.weather.model.entity.WeatherDailyEntity
import net.zackzhang.code.haze.weather.model.entity.WeatherHourlyEntity

data class WeatherLocalEntity(
    @Embedded
    val city: CityWeatherEntity,
    @Relation(
        parentColumn = Constants.ID,
        entityColumn = Constants.CITY_ID,
    )
    val hourlyList: List<WeatherHourlyEntity>,
    @Relation(
        parentColumn = Constants.ID,
        entityColumn = Constants.CITY_ID,
    )
    val dailyList: List<WeatherDailyEntity>,
    @Relation(
        parentColumn = Constants.ID,
        entityColumn = Constants.CITY_ID,
    )
    val airList: List<AirNowEntity>,
)