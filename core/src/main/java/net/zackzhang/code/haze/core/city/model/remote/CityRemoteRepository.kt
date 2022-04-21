package net.zackzhang.code.haze.core.city.model.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.zackzhang.code.haze.base.util.QWEATHER_PUBLIC_ID
import net.zackzhang.code.haze.base.util.makeSignature
import net.zackzhang.code.haze.base.util.seconds
import retrofit2.Retrofit

object CityRemoteRepository {

    private const val BASE_URL = "https://geoapi.qweather.com/v2/city/"

    private val SERVICES = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(CityConverterFactory())
        .build()
        .create(CityServices::class.java)

    suspend fun getCityList(location: String) = withContext(Dispatchers.IO) {
        val time = seconds
        SERVICES.getSearchAssociation(
            location,
            QWEATHER_PUBLIC_ID,
            time,
            makeSignature(location, time)
        )
    }
}