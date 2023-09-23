package net.zackzhang.code.haze.weather.model.remote

import net.zackzhang.code.haze.common.constant.QWEATHER_PARAM_LOCATION
import net.zackzhang.code.haze.common.constant.QWEATHER_PARAM_PUBLIC_ID
import net.zackzhang.code.haze.common.constant.QWEATHER_PARAM_SIGNATURE
import net.zackzhang.code.haze.common.constant.QWEATHER_PARAM_TIME
import net.zackzhang.code.haze.weather.model.entity.WeatherDailyEntity
import net.zackzhang.code.haze.weather.model.entity.WeatherHourlyEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServices {

    @GET("now")
    suspend fun getNow(
        @Query(QWEATHER_PARAM_LOCATION) location: String,
        @Query(QWEATHER_PARAM_PUBLIC_ID) publicId: String,
        @Query(QWEATHER_PARAM_TIME) time: String,
        @Query(QWEATHER_PARAM_SIGNATURE) signature: String
    ): WeatherHourlyEntity

    @GET("24h")
    suspend fun getHourly(
        @Query(QWEATHER_PARAM_LOCATION) location: String,
        @Query(QWEATHER_PARAM_PUBLIC_ID) publicId: String,
        @Query(QWEATHER_PARAM_TIME) time: String,
        @Query(QWEATHER_PARAM_SIGNATURE) signature: String
    ): List<WeatherHourlyEntity>

    @GET("7d")
    suspend fun getDaily(
        @Query(QWEATHER_PARAM_LOCATION) location: String,
        @Query(QWEATHER_PARAM_PUBLIC_ID) publicId: String,
        @Query(QWEATHER_PARAM_TIME) time: String,
        @Query(QWEATHER_PARAM_SIGNATURE) signature: String
    ): List<WeatherDailyEntity>
}