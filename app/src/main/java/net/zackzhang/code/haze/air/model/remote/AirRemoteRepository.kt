package net.zackzhang.code.haze.air.model.remote

import net.zackzhang.code.haze.common.util.QWEATHER_PUBLIC_ID
import retrofit2.Retrofit

object AirRemoteRepository {

    private const val BASE_URL = "https://devapi.qweather.com/v7/air/"

    private val SERVICES = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(AirConverterFactory())
        .build()
        .create(AirServices::class.java)

    suspend fun getAir(cityId: String, time: String, sign: String) =
        // 国内城市才有空气质量数据
        // 暂时使用 cityID 以 10 开头判断
        if (cityId.startsWith("10")) {
            SERVICES.getNow(cityId, QWEATHER_PUBLIC_ID, time, sign)
        } else null
}