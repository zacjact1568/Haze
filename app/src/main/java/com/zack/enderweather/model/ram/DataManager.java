package com.zack.enderweather.model.ram;

import com.zack.enderweather.model.bean.HeWeather;
import com.zack.enderweather.model.bean.Weather;
import com.zack.enderweather.model.database.DatabaseDispatcher;
import com.zack.enderweather.event.WeatherUpdateStatusChangedEvent;
import com.zack.enderweather.model.network.NetworkHelper;
import com.zack.enderweather.util.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static final String LOG_TAG = "DataManager";

    private DatabaseDispatcher databaseDispatcher;
    private List<Weather> weatherList;
    private boolean isWeatherDataLoaded = false;

    private static DataManager ourInstance = new DataManager();

    private DataManager() {
        databaseDispatcher = DatabaseDispatcher.getInstance();
        weatherList = new ArrayList<>();
    }

    public static DataManager getInstance() {
        return ourInstance;
    }

    /** 从数据库读取各个城市的天气数据<br>注：将来可能将其改造成异步读取 */
    public void loadFromDatabase() {
        if (!isWeatherDataLoaded) {
            isWeatherDataLoaded = true;
            weatherList.addAll(databaseDispatcher.loadWeather());
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
        markWeatherDataAsDeleted(location);
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
        return getWeather(location).getStatus() == Weather.STATUS_ON_UPDATING;
    }

    /** 设定天气数据的更新状态 */
    public void setWeatherDataUpdateStatus(int location, boolean isOnUpdating) {
        getWeather(location).setStatus(isOnUpdating ? Weather.STATUS_ON_UPDATING : Weather.STATUS_GENERAL);
    }

    /** 将天气数据标记为已删除 */
    public void markWeatherDataAsDeleted(int location) {
        getWeather(location).setStatus(Weather.STATUS_DELETED);
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
        int position = getLocationInWeatherList(cityId);
        //标记刷新状态
        setWeatherDataUpdateStatus(position, true);
        //发送开始更新的事件，通知presenters更新
        EventBus.getDefault().post(new WeatherUpdateStatusChangedEvent(
                position,
                cityId,
                WeatherUpdateStatusChangedEvent.STATUS_ON_UPDATING
        ));
        //异步发起网络访问
        new NetworkHelper().getHeWeatherDataAsync(cityId, new NetworkHelper.HeWeatherDataCallback() {
            @Override
            public void onSuccess(HeWeather heWeather) {
                String cityId = heWeather.getHeWeatherAPIList().get(0).getBasic().getId();
                int position = getLocationInWeatherList(cityId);
                Weather weather = getWeather(position);
                Util.parseHeWeatherData(heWeather, weather);
                databaseDispatcher.updateWeather(weather);
                //取消刷新状态
                setWeatherDataUpdateStatus(position, false);
                //发送更新完成的事件，通知presenters更新
                EventBus.getDefault().post(new WeatherUpdateStatusChangedEvent(
                        position,
                        cityId,
                        WeatherUpdateStatusChangedEvent.STATUS_UPDATED_SUCCESSFUL
                ));
            }

            @Override
            public void onFailure(String cityId) {
                int position = getLocationInWeatherList(cityId);
                setWeatherDataUpdateStatus(position, false);
                EventBus.getDefault().post(new WeatherUpdateStatusChangedEvent(
                        position,
                        cityId,
                        WeatherUpdateStatusChangedEvent.STATUS_UPDATED_FAILED
                ));
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
