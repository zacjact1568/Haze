package net.zackzhang.code.haze.weather.model.remote

import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import net.zackzhang.code.haze.air.model.remote.AirRemoteRepository
import net.zackzhang.code.haze.common.util.QWEATHER_PUBLIC_ID
import net.zackzhang.code.haze.common.util.makeSignature
import net.zackzhang.code.haze.common.util.seconds
import net.zackzhang.code.haze.weather.model.entity.WeatherEntity
import net.zackzhang.code.util.eLog
import retrofit2.Retrofit

object WeatherRemoteRepository {

    private const val BASE_URL = "https://devapi.qweather.com/v7/weather/"

    private val SERVICES = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(WeatherConverterFactory())
        .build()
        .create(WeatherServices::class.java)

    /**
     * 网络获取天气数据，失败返回 null
     * SupervisorJob 是为了阻止异常向上层协程传递
     * Retrofit 的协程实现调用了 enqueue，无需使用 Dispatchers.IO
     */
    suspend fun getWeather(cityId: String) = supervisorScope {
        val time = seconds
        val sign = makeSignature(cityId, time)
        val now = async {
            SERVICES.getNow(cityId, QWEATHER_PUBLIC_ID, time, sign)
        }
        val hourly = async {
            SERVICES.getHourly(cityId, QWEATHER_PUBLIC_ID, time, sign)
        }
        val daily = async {
            SERVICES.getDaily(cityId, QWEATHER_PUBLIC_ID, time, sign)
        }
        val air = async {
            AirRemoteRepository.getAir(cityId, time, sign)
        }
        runCatching {
            WeatherEntity(now.await(), hourly.await(), daily.await(), air.await())
        }.onSuccess {
            it.attachCityId(cityId)
        }.onFailure {
            eLog(this::class, "getWeather", "onFailure: ${it.message}")
            now.cancel()
            hourly.cancel()
            daily.cancel()
            air.cancel()
        }.getOrNull()
    }
}