package com.zack.enderweather.presenter;

import com.zack.enderweather.bean.Weather;
import com.zack.enderweather.view.WeatherView;

public class WeatherPresenter implements Presenter<WeatherView> {

    private WeatherView weatherView;
    private Weather weather;

    public WeatherPresenter(WeatherView weatherView, Weather weather) {
        attachView(weatherView);
        this.weather = weather;
    }

    @Override
    public void attachView(WeatherView view) {
        weatherView = view;
    }

    @Override
    public void detachView() {
        weatherView = null;
    }

    public void setInitialView() {
        weatherView.showInitialView(
                weather.getBasicInfo().getCityName(),
                weather.getBasicInfo().getUpdateTime(),
                weather.getCurrentInfo().getTemperature(),
                weather.getCurrentInfo().getCondition()
        );
    }
}
