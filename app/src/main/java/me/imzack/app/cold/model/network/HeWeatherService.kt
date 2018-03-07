package me.imzack.app.cold.model.network

import io.reactivex.Observable
import me.imzack.app.cold.model.bean.HeWeather

import retrofit2.http.GET
import retrofit2.http.Query

/** 和风天气 */
interface HeWeatherService {

    /** 常规天气数据集合 */
    @GET("weather")
    fun getHeWeatherCommonData(@Query("location") location: String, @Query("username") username: String, @Query("t") t: String, @Query("sign") sign: String): Observable<HeWeather.Common>

    /** 空气质量数据集合 */
    @GET("air")
    fun getHeWeatherAirData(@Query("location") location: String, @Query("username") username: String, @Query("t") t: String, @Query("sign") sign: String): Observable<HeWeather.Air>
}
