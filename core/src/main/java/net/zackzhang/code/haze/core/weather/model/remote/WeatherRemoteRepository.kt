package net.zackzhang.code.haze.core.weather.model.remote

import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import net.zackzhang.code.haze.core.air.model.remote.AirRemoteRepository
import net.zackzhang.code.haze.base.util.QWEATHER_PUBLIC_ID
import net.zackzhang.code.haze.base.util.eLog
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
            eLog("getWeather onFailure: ${it.message}", "WeatherRemoteRepository")
            now.cancel()
            hourly.cancel()
            daily.cancel()
            air.cancel()
        }.getOrNull()
    }
}