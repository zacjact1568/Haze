package net.zackzhang.code.haze.models.sources

import net.zackzhang.code.haze.utils.fromJsonArray
import net.zackzhang.code.haze.utils.fromJsonObject
import net.zackzhang.code.haze.utils.getUpdateTime
import net.zackzhang.code.haze.utils.toJsonObject
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

val WeatherServices: QWeatherServices = Retrofit.Builder()
    .baseUrl("https://devapi.qweather.com/v7/weather/")
    .addConverterFactory(WeatherConverterFactory())
    .build()
    .create(QWeatherServices::class.java)

interface QWeatherServices {

    @GET("now")
    suspend fun getNow(
        @Query("location") location: String,
        @Query("publicid") publicId: String,
        @Query("t") time: String,
        @Query("sign") signature: String
    ): WeatherHourlyEntity

    @GET("24h")
    suspend fun getHourly(
        @Query("location") location: String,
        @Query("publicid") publicId: String,
        @Query("t") time: String,
        @Query("sign") signature: String
    ): List<WeatherHourlyEntity>

    @GET("7d")
    suspend fun getDaily(
        @Query("location") location: String,
        @Query("publicid") publicId: String,
        @Query("t") time: String,
        @Query("sign") signature: String
    ): List<WeatherDailyEntity>
}

private class WeatherConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return when (type) {
            WeatherHourlyEntity::class.java -> WeatherNowConverter()
            is ParameterizedType -> when (type.actualTypeArguments[0]) {
                WeatherHourlyEntity::class.java -> WeatherHourlyConverter()
                WeatherDailyEntity::class.java -> WeatherDailyConverter()
                else -> null
            }
            else -> null
        }
    }
}

private class WeatherNowConverter : Converter<ResponseBody, WeatherHourlyEntity> {

    override fun convert(value: ResponseBody): WeatherHourlyEntity {
        val response = value.toJsonObject()
        return AppGson.fromJsonObject(
            response.getJSONObject("now"),
            WeatherHourlyEntity::class,
        ).apply {
            updatedAt = response.getUpdateTime()
        }
    }
}

private class WeatherHourlyConverter : Converter<ResponseBody, List<WeatherHourlyEntity>> {

    override fun convert(value: ResponseBody): List<WeatherHourlyEntity> {
        val response = value.toJsonObject()
        val updateTime = response.getUpdateTime()
        val hourlyList = AppGson.fromJsonArray(
            response.getJSONArray("hourly"),
            WeatherHourlyEntity::class,
        )
        hourlyList.forEach { it.updatedAt = updateTime }
        return hourlyList
    }
}

private class WeatherDailyConverter : Converter<ResponseBody, List<WeatherDailyEntity>> {

    override fun convert(value: ResponseBody): List<WeatherDailyEntity> {
        val response = value.toJsonObject()
        val updateTime = response.getUpdateTime()
        val dailyList = AppGson.fromJsonArray(
            response.getJSONArray("daily"),
            WeatherDailyEntity::class,
        )
        dailyList.forEach { it.updatedAt = updateTime }
        return dailyList
    }
}