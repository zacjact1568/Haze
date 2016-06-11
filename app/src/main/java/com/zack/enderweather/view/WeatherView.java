package com.zack.enderweather.view;

import com.zack.enderweather.bean.FormattedWeather;

public interface WeatherView {

    void showInitialView(FormattedWeather formattedWeather);

    void onDetectNetworkNotAvailable();

    void onWeatherUpdatedSuccessfully(FormattedWeather formattedWeather);

    void onWeatherUpdatedAbortively();

    void onChangeSwipeRefreshingStatus(boolean isRefreshing);
}
