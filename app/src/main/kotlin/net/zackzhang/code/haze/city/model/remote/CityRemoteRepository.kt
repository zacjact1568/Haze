package net.zackzhang.code.haze.city.model.remote

import net.zackzhang.code.haze.common.util.QWEATHER_PUBLIC_ID
import net.zackzhang.code.haze.common.util.makeSignature
import net.zackzhang.code.haze.common.util.seconds
import net.zackzhang.code.haze.city.model.entity.CityEntity
import retrofit2.Retrofit

object CityRemoteRepository {

    private const val BASE_URL = "https://geoapi.qweather.com/v2/city/"

    private val SERVICES = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(CityConverterFactory())
        .build()
        .create(CityServices::class.java)

    suspend fun getCityList(location: String): List<CityEntity> {
        val time = seconds
        return SERVICES.getSearchAssociation(
            location,
            QWEATHER_PUBLIC_ID,
            time,
            makeSignature(location, time)
        )
    }
}