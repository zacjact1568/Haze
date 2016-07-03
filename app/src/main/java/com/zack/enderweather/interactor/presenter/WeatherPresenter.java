package com.zack.enderweather.interactor.presenter;

import android.content.res.Resources;

import com.zack.enderweather.R;
import com.zack.enderweather.application.EnderWeatherApp;
import com.zack.enderweather.model.bean.DailyForecast;
import com.zack.enderweather.model.bean.FormattedWeather;
import com.zack.enderweather.model.bean.Weather;
import com.zack.enderweather.event.WeatherUpdateStatusChangedEvent;
import com.zack.enderweather.model.ram.DataManager;
import com.zack.enderweather.util.Util;
import com.zack.enderweather.domain.view.WeatherView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class WeatherPresenter implements Presenter<WeatherView> {

    private static final String LOG_TAG = "WeatherPresenter";

    private WeatherView weatherView;
    private DataManager dataManager;
    private Weather weather;
    private String updateTimeFormat, invalidDataStr;
    //private boolean isVisible;

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
                    null
            );
        } else {
            fw = getFormattedWeather();
        }
        weatherView.showInitialView(fw);
    }

    public void notifyWeatherUpdating() {
        if (Util.isNetworkAvailable()) {
            dataManager.getWeatherDataFromInternet(weather.getBasicInfo().getCityId());
        } else {
            //不会和HomeActivity、MyCitiesFragment中的SnackBar同时出现
            weatherView.onDetectNetworkNotAvailable();
        }
    }

    public void notifyVisibilityChanged(boolean isVisible) {
        //this.isVisible = isVisible;
        //如果当前fragment变为不可见，直接隐藏下拉刷新图标；如果当前fragment变为可见，且又是在刷新状态时，显示下拉刷新图标
        weatherView.onChangeSwipeRefreshingStatus(isVisible && weather.getStatus() == Weather.STATUS_ON_UPDATING);
    }

    private FormattedWeather getFormattedWeather() {
        //String[] dates = new String[Weather.DAILY_FORECAST_LENGTH];
        String[] conditions = new String[Weather.DAILY_FORECAST_LENGTH_DISPLAY];
        //String[] tempRanges = new String[Weather.DAILY_FORECAST_LENGTH];
        int[] maxTemps = new int[Weather.DAILY_FORECAST_LENGTH_DISPLAY];
        int[] minTemps = new int[Weather.DAILY_FORECAST_LENGTH_DISPLAY];
        for (int i = 0; i < Weather.DAILY_FORECAST_LENGTH_DISPLAY; i++) {
            DailyForecast df = weather.getDailyForecastList().get(i);
            //dates[i] = df.getDate();
            conditions[i] = df.getConditionDay();
            //tempRanges[i] = String.format("%s | %s", df.getMinTemp(), df.getMaxTemp());
            maxTemps[i] = Integer.parseInt(df.getMaxTemp());
            minTemps[i] = Integer.parseInt(df.getMinTemp());
        }
        return new FormattedWeather(
                weather.getBasicInfo().getCityName(),
                weather.getCurrentInfo().getCondition(),
                weather.getCurrentInfo().getTemperature(),
                String.format(updateTimeFormat, weather.getBasicInfo().getUpdateTime()),
                String.format("%s°C", weather.getCurrentInfo().getSensibleTemp()),
                String.format("%s | %s", weather.getDailyForecastList().get(0).getMinTemp(), weather.getDailyForecastList().get(0).getMaxTemp()),
                weather.getAirQuality().getAqi().isEmpty() ? "--" : weather.getAirQuality().getAqi(),
                Util.generateWeeks(weather.getDailyForecastList().get(0).getDate(), Weather.DAILY_FORECAST_LENGTH_DISPLAY),
                conditions,
                maxTemps,
                minTemps
        );
    }

    @Subscribe
    public void onWeatherUpdateStatusChanged(WeatherUpdateStatusChangedEvent event) {
        //当WeatherUpdated事件发生时，这个方法总会被调用，但MyCitiesPresenter中的订阅方法不一定被执行
        if (weather.getBasicInfo().getCityId().equals(event.cityId)) {
            //说明更新的是当前城市
            switch (event.status) {
                case WeatherUpdateStatusChangedEvent.STATUS_ON_UPDATING:
                    //weatherView.onChangeSwipeRefreshingStatus(isVisible); TODO 未触发，isVisible为false
                    break;
                case WeatherUpdateStatusChangedEvent.STATUS_UPDATED_SUCCESSFUL:
                    weatherView.onWeatherUpdatedSuccessfully(getFormattedWeather());
                    break;
                case WeatherUpdateStatusChangedEvent.STATUS_UPDATED_FAILED:
                    weatherView.onWeatherUpdatedAbortively();
                    break;
                default:
                    break;
            }
        }
    }
}
