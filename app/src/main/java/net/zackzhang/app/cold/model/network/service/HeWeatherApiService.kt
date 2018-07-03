package net.zackzhang.app.cold.model.network.service

import io.reactivex.Single
import net.zackzhang.app.cold.model.network.bean.HeWeatherApi
import retrofit2.http.GET
import retrofit2.http.Query

/** 和风天气 API */
// 按 API 组织
interface HeWeatherApiService {

    companion object {
        const val TYPE_WEATHER = "weather"
        const val TYPE_AIR = "air"
    }

    /** 常规天气数据集合 */
    @GET("weather")
    fun getWeatherData(@Query("location") location: String, @Query("username") username: String, @Query("t") t: String, @Query("sign") sign: String): Single<HeWeatherApi.Weather>

    /** 空气质量数据集合 */
    @GET("air")
    fun getAirData(@Query("location") location: String, @Query("username") username: String, @Query("t") t: String, @Query("sign") sign: String): Single<HeWeatherApi.Air>
}
