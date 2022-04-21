package net.zackzhang.code.haze.core.weather.model.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import net.zackzhang.code.haze.core.air.model.remote.AirRemoteRepository
import net.zackzhang.code.haze.base.util.QWEATHER_PUBLIC_ID
import net.zackzhang.code.haze.base.util.makeSignature
import net.zackzhang.code.haze.base.util.seconds
import net.zackzhang.code.haze.core.weather.model.entity.WeatherEntity
import retrofit2.Retrofit

object WeatherRemoteRepository {

    private const val BASE_URL = "https://devapi.qweather.com/v7/weather/"

    private val SERVICES = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(WeatherConverterFactory())
        .build()
        .create(WeatherServices::class.java)

    suspend fun getWeather(cityId: String) = withContext(Dispatchers.IO) {
        val time = seconds
        val sign = makeSignature(cityId, time)
        val now = async { SERVICES.getNow(cityId, QWEATHER_PUBLIC_ID, time, sign) }
        val hourly = async { SERVICES.getHourly(cityId, QWEATHER_PUBLIC_ID, time, sign) }
        val daily = async { SERVICES.getDaily(cityId, QWEATHER_PUBLIC_ID, time, sign) }
        val air = async { AirRemoteRepository.getAir(cityId, time, sign) }
        WeatherEntity(now.await(), hourly.await(), daily.await(), air.await()).apply {
            attachCityId(cityId)
        }
    }
}