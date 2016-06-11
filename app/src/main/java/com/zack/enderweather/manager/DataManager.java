package com.zack.enderweather.manager;

import com.zack.enderweather.bean.HeWeather;
import com.zack.enderweather.bean.Weather;
import com.zack.enderweather.database.EnderWeatherDB;
import com.zack.enderweather.event.WeatherUpdatedEvent;
import com.zack.enderweather.network.NetworkHelper;
import com.zack.enderweather.util.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static final String LOG_TAG = "DataManager";

    private EnderWeatherDB enderWeatherDB;
    private List<Weather> weatherList;
    private boolean isWeatherDataLoaded = false;

    private static DataManager ourInstance = new DataManager();

    private DataManager() {
        enderWeatherDB = EnderWeatherDB.getInstance();
        weatherList = new ArrayList<>();
    }

    public static DataManager getInstance() {
        return ourInstance;
    }

    /** 从数据库读取各个城市的天气数据<br>注：将来可能将其改造成异步读取 */
    public void loadFromDatabase() {
        if (!isWeatherDataLoaded) {
            isWeatherDataLoaded = true;
            weatherList.addAll(enderWeatherDB.loadWeather());
        }
    }

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public Weather getWeather(int location) {
        return weatherList.get(location);
    }

    public Weather getWeather(String cityId) {
        return weatherList.get(getLocationInWeatherList(cityId));
    }

    public void addToWeatherList(String cityId, String cityName) {
        weatherList.add(new Weather(cityId, cityName));
    }

    public void removeFromWeatherList(int location) {
        weatherList.remove(location);
    }

    public int getWeatherCount() {
        return weatherList.size();
    }

    public String getCityId(int location) {
        return getWeather(location).getBasicInfo().getCityId();
    }

    public String getCityName(int location) {
        return getWeather(location).getBasicInfo().getCityName();
    }

    /** 获取天气数据的更新状态 */
    public boolean getWeatherDataUpdateStatus(int location) {
        return getWeather(location).getIsOnUpdate();
    }

    /** 设定天气数据的更新状态 */
    public void setWeatherDataUpdateStatus(int location, boolean isOnUpdate) {
        getWeather(location).setIsOnUpdate(isOnUpdate);
    }

    /** 获取最近添加的位置（末尾）*/
    public int getRecentlyAddedLocation() {
        return getWeatherCount() - 1;
    }

    /** 获取最近添加的天气（末尾）*/
    public Weather getRecentlyAddedWeather() {
        return getWeather(getRecentlyAddedLocation());
    }

    /** 获取最近添加的城市ID（末尾）*/
    public String getRecentlyAddedCityId() {
        return getRecentlyAddedWeather().getBasicInfo().getCityId();
    }

    /** 检测城市是否已存在 */
    public boolean isCityExists(String cityId) {
        for (Weather weather : weatherList) {
            if (weather.getBasicInfo().getCityId().equals(cityId)) {
                return true;
            }
        }
        return false;
    }

    /** 发起网络访问，获取天气数据 */
    public void getWeatherDataFromInternet(String cityId) {
        new NetworkHelper().getHeWeatherDataAsync(cityId, new NetworkHelper.HeWeatherDataCallback() {
            @Override
            public void onSuccess(HeWeather heWeather) {
                String cityId = heWeather.getHeWeatherAPIList().get(0).getBasic().getId();
                int position = getLocationInWeatherList(cityId);
                Weather weather = getWeather(position);
                Util.parseHeWeatherData(heWeather, weather);
                enderWeatherDB.updateWeather(weather);
                EventBus.getDefault().post(new WeatherUpdatedEvent(position, cityId, true));
            }

            @Override
            public void onFailure(String cityId) {
                EventBus.getDefault().post(new WeatherUpdatedEvent(getLocationInWeatherList(cityId), cityId, false));
            }
        });
    }

    public int getLocationInWeatherList(String cityId) {
        for (int i = 0; i < getWeatherCount(); i++) {
            if (getWeather(i).getBasicInfo().getCityId().equals(cityId)) {
                return i;
            }
        }
        throw new RuntimeException("No Weather object matching city id " + cityId);
    }
}
