package com.zack.enderweather.presenter;

import android.content.Context;

import com.zack.enderweather.R;
import com.zack.enderweather.application.EnderWeatherApp;
import com.zack.enderweather.bean.DailyForecast;
import com.zack.enderweather.bean.FormattedWeather;
import com.zack.enderweather.bean.Weather;
import com.zack.enderweather.event.WeatherUpdatedEvent;
import com.zack.enderweather.util.Util;
import com.zack.enderweather.view.WeatherView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class WeatherPresenter implements Presenter<WeatherView> {

    private WeatherView weatherView;
    private Weather weather;
    private String updateTimeFormat, invalidDataStr;

    public WeatherPresenter(WeatherView weatherView, Weather weather) {
        attachView(weatherView);
        this.weather = weather;

        Context context = EnderWeatherApp.getGlobalContext();
        updateTimeFormat = context.getResources().getString(R.string.format_update_time);
        invalidDataStr = context.getResources().getString(R.string.text_invalid_data);
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
        //判断数据是否可用
        boolean isDataValid = weather.getBasicInfo().getUpdateTime() != null;
        FormattedWeather fw = new FormattedWeather(weather.getBasicInfo().getCityName());
        if (isDataValid) {
            String[] dates = new String[Weather.DAILY_FORECAST_LENGTH];
            String[] conditions = new String[Weather.DAILY_FORECAST_LENGTH];
            String[] tempRanges = new String[Weather.DAILY_FORECAST_LENGTH];
            for (int i = 0; i < Weather.DAILY_FORECAST_LENGTH; i++) {
                DailyForecast df = weather.getDailyForecastList().get(i);
                dates[i] = df.getDate();
                conditions[i] = df.getConditionDay();
                tempRanges[i] = String.format("%s | %s", df.getMinTemp(), df.getMaxTemp());
            }
            fw.setExtraValues(
                    weather.getCurrentInfo().getCondition(),
                    weather.getCurrentInfo().getTemperature(),
                    String.format(updateTimeFormat, weather.getBasicInfo().getUpdateTime()),
                    String.format("%s°C", weather.getCurrentInfo().getSensibleTemp()),
                    String.format("%s | %s", weather.getDailyForecastList().get(0).getMinTemp(), weather.getDailyForecastList().get(0).getMaxTemp()),
                    Util.parseAqi(weather.getAirQuality().getAqi()),
                    Util.generateWeeks(weather.getDailyForecastList().get(0).getDate()),
                    dates,
                    conditions,
                    tempRanges
            );
        } else {
            fw.setExtraValues(
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
        }
        weatherView.showInitialView(fw);
    }

    @Subscribe
    public void onWeatherUpdated(WeatherUpdatedEvent event) {
        if (weather.getBasicInfo().getCityId().equals(event.cityId)) {
            //说明更新的是当前城市
            setInitialView();//TODO 不使用setINITIALView
        }
    }
}
