package net.zackzhang.code.haze.core.weather.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import net.zackzhang.code.haze.core.air.model.entity.AirNowEntity
import net.zackzhang.code.haze.core.common.constant.*
import net.zackzhang.code.haze.core.weather.model.entity.WeatherDailyEntity
import net.zackzhang.code.haze.core.weather.model.entity.WeatherHourlyEntity
import net.zackzhang.code.haze.core.weather.model.entity.WeatherLocalEntity

@Dao
interface WeatherDao {

    @Insert
    suspend fun insert(hourlyList: List<WeatherHourlyEntity>, dailyList: List<WeatherDailyEntity>, airList: List<AirNowEntity>)

    @Query("DELETE FROM $WEATHER_HOURLY WHERE $CITY_ID = :cityId")
    suspend fun deleteHourly(cityId: String)

    @Query("DELETE FROM $WEATHER_DAILY WHERE $CITY_ID = :cityId")
    suspend fun deleteDaily(cityId: String)

    @Query("DELETE FROM $AIR WHERE $CITY_ID = :cityId")
    suspend fun deleteAir(cityId: String)

    @Transaction
    suspend fun replace(cityId: String, hourlyList: List<WeatherHourlyEntity>, dailyList: List<WeatherDailyEntity>, airList: List<AirNowEntity>) {
        deleteHourly(cityId)
        deleteDaily(cityId)
        deleteAir(cityId)
        insert(hourlyList, dailyList, airList)
    }

    @Transaction
    @Query("SELECT $ID, $NAME FROM $CITY ORDER BY $CREATED_AT")
    suspend fun query(): List<WeatherLocalEntity>
}