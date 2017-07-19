package me.imzack.app.cold.domain.view;

import me.imzack.app.cold.model.bean.FormattedWeather;

public interface WeatherView {

    void showInitialView(FormattedWeather formattedWeather);

    void onDetectNetworkNotAvailable();

    void onWeatherUpdatedSuccessfully(FormattedWeather formattedWeather);

    void onWeatherUpdatedAbortively();

    void onChangeSwipeRefreshingStatus(boolean isRefreshing);
}
