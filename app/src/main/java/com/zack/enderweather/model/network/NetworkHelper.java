package com.zack.enderweather.model.network;

import com.zack.enderweather.model.bean.HeWeather;
import com.zack.enderweather.util.LogUtil;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkHelper {

    private static final String LOG_TAG = "NetworkHelper";

    private static final String URL_HE_WEATHER_API = "https://api.heweather.com/x3/";
    private static final String KEY_HE_WEATHER_API = "b62ec297adac4f64a4aabdc2f86e1ce7";

    /** 异步获取和风天气数据 */
    public void getHeWeatherDataAsync(String cityId, final HeWeatherDataCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_HE_WEATHER_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<HeWeather> call = service.getHeWeatherData(cityId, KEY_HE_WEATHER_API);
        call.enqueue(new Callback<HeWeather>() {
            @Override
            public void onResponse(Call<HeWeather> call, Response<HeWeather> response) {
                if (response.body() != null) {
                    //解析成功
                    callback.onSuccess(response.body());
                } else {
                    try {
                        LogUtil.e(LOG_TAG, response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callback.onFailure(call.request().url().queryParameter("cityid"));
                }
            }

            @Override
            public void onFailure(Call<HeWeather> call, Throwable t) {
                LogUtil.e(LOG_TAG, t.getMessage());
                callback.onFailure(call.request().url().queryParameter("cityid"));
            }
        });
    }

    public interface HeWeatherDataCallback {
        void onSuccess(HeWeather heWeather);
        void onFailure(String cityId);
    }
}
