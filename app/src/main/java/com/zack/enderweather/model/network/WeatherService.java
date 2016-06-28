package com.zack.enderweather.model.network;

import com.zack.enderweather.model.bean.HeWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("weather")
    Call<HeWeather> getHeWeatherData(@Query("cityid") String cityId, @Query("key") String key);
}
