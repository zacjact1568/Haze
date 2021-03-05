package net.zackzhang.code.haze.weather.model.remote

import net.zackzhang.code.haze.common.Constants
import net.zackzhang.code.haze.weather.model.entity.WeatherDailyEntity
import net.zackzhang.code.haze.weather.model.entity.WeatherHourlyEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServices {

    @GET("now")
    suspend fun getNow(
        @Query(Constants.QWEATHER_PARAM_LOCATION) location: String,
        @Query(Constants.QWEATHER_PARAM_PUBLIC_ID) publicId: String,
        @Query(Constants.QWEATHER_PARAM_TIME) time: String,
        @Query(Constants.QWEATHER_PARAM_SIGNATURE) signature: String
    ): WeatherHourlyEntity

    @GET("24h")
    suspend fun getHourly(
        @Query(Constants.QWEATHER_PARAM_LOCATION) location: String,
        @Query(Constants.QWEATHER_PARAM_PUBLIC_ID) publicId: String,
        @Query(Constants.QWEATHER_PARAM_TIME) time: String,
        @Query(Constants.QWEATHER_PARAM_SIGNATURE) signature: String
    ): List<WeatherHourlyEntity>

    @GET("7d")
    suspend fun getDaily(
        @Query(Constants.QWEATHER_PARAM_LOCATION) location: String,
        @Query(Constants.QWEATHER_PARAM_PUBLIC_ID) publicId: String,
        @Query(Constants.QWEATHER_PARAM_TIME) time: String,
        @Query(Constants.QWEATHER_PARAM_SIGNATURE) signature: String
    ): List<WeatherDailyEntity>
}