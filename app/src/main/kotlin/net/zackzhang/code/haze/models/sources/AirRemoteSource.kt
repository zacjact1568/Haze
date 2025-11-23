package net.zackzhang.code.haze.models.sources

import net.zackzhang.code.haze.models.AirEntity
import net.zackzhang.code.haze.utils.fromJsonObject
import net.zackzhang.code.haze.utils.getUpdateTime
import net.zackzhang.code.haze.utils.toJsonObject
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.reflect.Type

val AirServices: QWeatherAirServices = Retrofit.Builder()
    .baseUrl("https://devapi.qweather.com/v7/air/")
    .addConverterFactory(AirConverterFactory())
    .build()
    .create(QWeatherAirServices::class.java)

interface QWeatherAirServices {

    @GET("now")
    suspend fun getNow(
        @Query("location") location: String,
        @Query("publicid") publicId: String,
        @Query("t") time: String,
        @Query("sign") signature: String
    ): AirEntity
}

private class AirConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return when (type) {
            AirEntity::class.java -> AirConverter()
            else -> null
        }
    }
}

private class AirConverter : Converter<ResponseBody, AirEntity> {

    override fun convert(value: ResponseBody): AirEntity {
        val response = value.toJsonObject()
        val updateTime = response.getUpdateTime()
        val now = AppGson.fromJsonObject(
            response.getJSONObject("now"),
            AirNowEntity::class,
        ).apply {
            updatedAt = updateTime
        }
        val rawStationList = AppGson.fromJsonObject(response, "station", AirNowEntity::class)
        val stationList = rawStationList?.run {
            val map = linkedMapOf<String, AirNowEntity>()
            forEach {
                it.updatedAt = updateTime
                // 服务端可能返回多个相同的 stationId，去重
                map[it.stationId] = it
            }
            ArrayList(map.values)
        }
        return AirEntity(now, stationList)
    }
}