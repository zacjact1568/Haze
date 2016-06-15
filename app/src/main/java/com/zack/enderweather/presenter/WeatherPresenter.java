package com.zack.enderweather.presenter;

import android.content.res.Resources;

import com.zack.enderweather.R;
import com.zack.enderweather.application.EnderWeatherApp;
import com.zack.enderweather.bean.DailyForecast;
import com.zack.enderweather.bean.FormattedWeather;
import com.zack.enderweather.bean.Weather;
import com.zack.enderweather.event.WeatherUpdatedEvent;
import com.zack.enderweather.manager.DataManager;
import com.zack.enderweather.util.Util;
import com.zack.enderweather.view.WeatherView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class WeatherPresenter implements Presenter<WeatherView> {

    private static final String LOG_TAG = "WeatherPresenter";

    private WeatherView weatherView;
    private DataManager dataManager;
    private Weather weather;
    private String updateTimeFormat, invalidDataStr;

    public WeatherPresenter(WeatherView weatherView, Weather weather) {
        attachView(weatherView);
        dataManager = DataManager.getInstance();
        this.weather = weather;

        Resources resources = EnderWeatherApp.getGlobalContext().getResources();
        updateTimeFormat = resources.getString(R.string.format_update_time);
        invalidDataStr = resources.getString(R.string.text_invalid_data);
    }

    @Override
    public void attachView(WeatherView view) {
        weatherView = view;
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachView() {
        weatherView = null;
        EventBus.getDefault().unregister(this);
    }

    public void setInitialView() {
        FormattedWeather fw;
        if (weather.getBasicInfo().getUpdateTime().isEmpty()) {
            //说明数据为空
            fw = new FormattedWeather(
                    weather.getBasicInfo().getCityName(),
                    "--",
                    "--",
                    invalidDataStr,
                    "--°C",
                    "-- | --",
                    "--",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        } else {
            fw = assembleFormattedWeather();
        }
        weatherView.showInitialView(fw);
    }

    public void notifyWeatherUpdating() {
        if (Util.isNetworkAvailable()) {
            String cityId = weather.getBasicInfo().getCityId();
            dataManager.setWeatherDataUpdateStatus(dataManager.getLocationInWeatherList(cityId), true);
            dataManager.getWeatherDataFromInternet(cityId);
        } else {
            //不会和MyCitiesFragment中的SnackBar同时出现
            weatherView.onDetectNetworkNotAvailable();
        }
    }

    public void notifyVisibilityChanged(boolean isVisible) {
        //如果当前fragment变为不可见，直接隐藏下拉刷新图标；如果当前fragment变为可见，且又是在刷新状态时，显示下拉刷新图标
        weatherView.onChangeSwipeRefreshingStatus(isVisible && weather.getIsOnUpdate());
    }

    private FormattedWeather assembleFormattedWeather() {
        int[] maxTemps = new int[Weather.DAILY_FORECAST_LENGTH];
        int[] minTemps = new int[Weather.DAILY_FORECAST_LENGTH];
        String[] dates = new String[Weather.DAILY_FORECAST_LENGTH];
        String[] conditions = new String[Weather.DAILY_FORECAST_LENGTH];
        String[] tempRanges = new String[Weather.DAILY_FORECAST_LENGTH];
        for (int i = 0; i < Weather.DAILY_FORECAST_LENGTH; i++) {
            DailyForecast df = weather.getDailyForecastList().get(i);
            maxTemps[i] = Integer.parseInt(df.getMaxTemp());
            minTemps[i] = Integer.parseInt(df.getMinTemp());
            dates[i] = df.getDate();
            conditions[i] = df.getConditionDay();
            tempRanges[i] = String.format("%s | %s", df.getMinTemp(), df.getMaxTemp());
        }
        return new FormattedWeather(
                weather.getBasicInfo().getCityName(),
                weather.getCurrentInfo().getCondition(),
                weather.getCurrentInfo().getTemperature(),
                String.format(updateTimeFormat, weather.getBasicInfo().getUpdateTime()),
                String.format("%s°C", weather.getCurrentInfo().getSensibleTemp()),
                String.format("%s | %s", weather.getDailyForecastList().get(0).getMinTemp(), weather.getDailyForecastList().get(0).getMaxTemp()),
                weather.getAirQuality().getAqi().isEmpty() ? "--" : weather.getAirQuality().getAqi(),
                maxTemps,
                minTemps,
                Util.generateWeeks(weather.getDailyForecastList().get(0).getDate()),
                dates,
                conditions,
                tempRanges
        );
    }

    @Subscribe
    public void onWeatherUpdated(WeatherUpdatedEvent event) {
        //当WeatherUpdated事件发生时，这个方法总会被调用，但MyCitiesPresenter中的订阅方法不一定被执行
        if (weather.getBasicInfo().getCityId().equals(event.cityId)) {
            //说明更新的是当前城市
            if (event.isSuc) {
                weatherView.onWeatherUpdatedSuccessfully(assembleFormattedWeather());
            } else {
                weatherView.onWeatherUpdatedAbortively();
            }
        }
    }
}
