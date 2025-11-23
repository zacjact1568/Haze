package net.zackzhang.code.haze.models.sources

import net.zackzhang.code.haze.utils.fromJsonArray
import net.zackzhang.code.haze.utils.toJsonObject
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

val CityServices: QWeatherCityServices = Retrofit.Builder()
    .baseUrl("https://geoapi.qweather.com/v2/city/")
    .addConverterFactory(CityConverterFactory())
    .build()
    .create(QWeatherCityServices::class.java)

interface QWeatherCityServices {

    @GET("lookup")
    suspend fun getSearchAssociation(
        @Query("location") location: String,
        @Query("publicid") publicId: String,
        @Query("t") time: String,
        @Query("sign") signature: String
    ): List<CityEntity>
}

private class CityConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return when (type) {
            is ParameterizedType -> when (type.actualTypeArguments[0]) {
                CityEntity::class.java -> CityConverter()
                else -> null
            }
            else -> null
        }
    }
}

private class CityConverter : Converter<ResponseBody, List<CityEntity>> {

    override fun convert(value: ResponseBody): List<CityEntity> {
        val response = value.toJsonObject()
        return AppGson.fromJsonArray(
            response.getJSONArray("location"),
            CityEntity::class,
        )
    }
}