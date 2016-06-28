package com.zack.enderweather.domain.view;

import com.zack.enderweather.model.bean.FormattedWeather;

public interface WeatherView {

    void showInitialView(FormattedWeather formattedWeather);

    void onDetectNetworkNotAvailable();

    void onWeatherUpdatedSuccessfully(FormattedWeather formattedWeather);

    void onWeatherUpdatedAbortively();

    void onChangeSwipeRefreshingStatus(boolean isRefreshing);
}
