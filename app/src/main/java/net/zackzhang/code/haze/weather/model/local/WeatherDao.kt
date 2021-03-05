package net.zackzhang.code.haze.weather.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import net.zackzhang.code.haze.air.model.entity.AirNowEntity
import net.zackzhang.code.haze.common.Constants
import net.zackzhang.code.haze.weather.model.entity.WeatherDailyEntity
import net.zackzhang.code.haze.weather.model.entity.WeatherHourlyEntity
import net.zackzhang.code.haze.weather.model.entity.WeatherLocalEntity

@Dao
interface WeatherDao {

    @Insert
    suspend fun insert(hourlyList: List<WeatherHourlyEntity>, dailyList: List<WeatherDailyEntity>, airList: List<AirNowEntity>)

    @Query("DELETE FROM ${Constants.WEATHER_HOURLY} WHERE ${Constants.CITY_ID} = :cityId")
    suspend fun deleteHourly(cityId: String)

    @Query("DELETE FROM ${Constants.WEATHER_DAILY} WHERE ${Constants.CITY_ID} = :cityId")
    suspend fun deleteDaily(cityId: String)

    @Query("DELETE FROM ${Constants.AIR} WHERE ${Constants.CITY_ID} = :cityId")
    suspend fun deleteAir(cityId: String)

    @Transaction
    suspend fun replace(cityId: String, hourlyList: List<WeatherHourlyEntity>, dailyList: List<WeatherDailyEntity>, airList: List<AirNowEntity>) {
        deleteHourly(cityId)
        deleteDaily(cityId)
        deleteAir(cityId)
        insert(hourlyList, dailyList, airList)
    }

    @Transaction
    @Query("SELECT ${Constants.ID}, ${Constants.NAME} FROM ${Constants.CITY} ORDER BY ${Constants.CREATED_AT}")
    suspend fun query(): List<WeatherLocalEntity>
}