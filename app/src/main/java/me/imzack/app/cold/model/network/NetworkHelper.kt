package me.imzack.app.cold.model.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import me.imzack.app.cold.common.Constant
import me.imzack.app.cold.model.bean.HeWeather
import me.imzack.app.cold.util.LogUtil
import me.imzack.app.cold.util.WeatherUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NetworkHelper {

    /** 异步获取和风天气数据 */
    fun getHeWeatherDataAsync(cityId: String, callback: (HeWeather) -> Unit) {
        val service = Retrofit.Builder()
                .baseUrl(Constant.HE_WEATHER_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { LogUtil.i("HeWeather", it) }).setLevel(HttpLoggingInterceptor.Level.BODY))
                        .addNetworkInterceptor(StethoInterceptor())
                        .build())
                .build()
                .create(HeWeatherService::class.java)
        val time = (System.currentTimeMillis() / 1000).toString()
        val sign = WeatherUtil.makeHeWeatherApiSignature(mapOf(
                "location" to cityId,
                "username" to Constant.HE_WEATHER_USER_ID,
                "t" to time
        ))
        Observable.zip(
                service.getHeWeatherCommonData(cityId, Constant.HE_WEATHER_USER_ID, time, sign),
                service.getHeWeatherAirData(cityId, Constant.HE_WEATHER_USER_ID, time, sign),
                // SAM 转换
                BiFunction<HeWeather.Common, HeWeather.Air, HeWeather> { common, air -> HeWeather(common, air) }
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ callback(it) }, { LogUtil.e(cityId, it.message ?: "null") })
    }
}
