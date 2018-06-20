package me.imzack.app.cold.model.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import me.imzack.app.cold.common.Constant
import me.imzack.app.cold.model.network.bean.HeWeatherApi
import me.imzack.app.cold.model.network.service.HeWeatherApiService
import me.imzack.app.cold.model.network.service.HeWeatherSearchService
import me.imzack.app.cold.util.LogUtil
import me.imzack.app.cold.util.HeWeatherUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

// 按数据需求组织，不按 API 组织
class NetworkHelper {

    private val heWeatherApiService
        get() = Retrofit.Builder()
                .baseUrl(Constant.HE_WEATHER_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient("HeWeatherApiService"))
                .build()
                .create(HeWeatherApiService::class.java)

    /** 通过城市 id 获取和风天气与空气数据 */
    fun getHeWeatherAndAirData(cityId: String): Single<Pair<HeWeatherApi.Weather, HeWeatherApi.Air>> {
        val service = heWeatherApiService
        val time = HeWeatherUtil.apiSignatureTime
        val sign = HeWeatherUtil.makeApiSignature(cityId, time)
        return Single.zip(
                service.getWeatherData(cityId, Constant.HE_WEATHER_USER_ID, time, sign),
                service.getAirData(cityId, Constant.HE_WEATHER_USER_ID, time, sign),
                // SAM 转换
                BiFunction<HeWeatherApi.Weather, HeWeatherApi.Air, Pair<HeWeatherApi.Weather, HeWeatherApi.Air>> { hwaWeather, hwaAir -> Pair(hwaWeather, hwaAir) }
        )
    }

    /** 通过城市 id 获取和风天气数据 */
    fun getHeWeatherData(cityId: String): Single<HeWeatherApi.Weather> {
        val time = HeWeatherUtil.apiSignatureTime
        return heWeatherApiService.getWeatherData(cityId, Constant.HE_WEATHER_USER_ID, time, HeWeatherUtil.makeApiSignature(cityId, time))
    }

    /** 通过经纬度查询城市 id */
    fun getHeWeatherCityData(longitude: Double, latitude: Double) = Retrofit.Builder()
            .baseUrl(Constant.HE_WEATHER_SEARCH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getOkHttpClient("HeWeatherSearchService"))
            .build()
            .create(HeWeatherSearchService::class.java)
            .getFindData("$longitude,$latitude", Constant.HE_WEATHER_API_KEY)

    /**
     * 获取一个 OkHttpClient 对象
     * @param tag Logcat 打印的 tag
     */
    private fun getOkHttpClient(tag: String) = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { LogUtil.i(it, tag) }).setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(StethoInterceptor())
            .build()
}
