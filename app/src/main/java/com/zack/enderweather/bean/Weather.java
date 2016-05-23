package com.zack.enderweather.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Weather implements Parcelable {

    private BasicInfo basicInfo;
    private CurrentInfo currentInfo;
    private List<HourlyForecast> hourlyForecastList;
    private List<DailyForecast> dailyForecastList;
    private AirQuality airQuality;
    private LifeSuggestion lifeSuggestion;

    public static final int HOURLY_FORECAST_LENGTH = 8;
    public static final int DAILY_FORECAST_LENGTH = 7;

    public Weather(String cityId, String cityName) {
        this(
                new BasicInfo(cityId, cityName),
                new CurrentInfo(cityId),
                null,
                null,
                new AirQuality(cityId),
                new LifeSuggestion(cityId)
        );

        List<HourlyForecast> hourlyForecastList = new ArrayList<>();
        for (int i = 0; i < HOURLY_FORECAST_LENGTH; i++) {
            hourlyForecastList.add(new HourlyForecast(cityId));
        }
        setHourlyForecastList(hourlyForecastList);

        List<DailyForecast> dailyForecastList = new ArrayList<>();
        for (int i = 0; i < DAILY_FORECAST_LENGTH; i++) {
            dailyForecastList.add(new DailyForecast(cityId));
        }
        setDailyForecastList(dailyForecastList);
    }

    public Weather(BasicInfo basicInfo, CurrentInfo currentInfo, List<HourlyForecast> hourlyForecastList,
                   List<DailyForecast> dailyForecastList, AirQuality airQuality, LifeSuggestion lifeSuggestion) {
        this.basicInfo = basicInfo;
        this.currentInfo = currentInfo;
        this.hourlyForecastList = hourlyForecastList;
        this.dailyForecastList = dailyForecastList;
        this.airQuality = airQuality;
        this.lifeSuggestion = lifeSuggestion;
    }

    public BasicInfo getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(BasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }

    public CurrentInfo getCurrentInfo() {
        return currentInfo;
    }

    public void setCurrentInfo(CurrentInfo currentInfo) {
        this.currentInfo = currentInfo;
    }

    public List<HourlyForecast> getHourlyForecastList() {
        return hourlyForecastList;
    }

    public void setHourlyForecastList(List<HourlyForecast> hourlyForecastList) {
        this.hourlyForecastList = hourlyForecastList;
    }

    public List<DailyForecast> getDailyForecastList() {
        return dailyForecastList;
    }

    public void setDailyForecastList(List<DailyForecast> dailyForecastList) {
        this.dailyForecastList = dailyForecastList;
    }

    public AirQuality getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(AirQuality airQuality) {
        this.airQuality = airQuality;
    }

    public LifeSuggestion getLifeSuggestion() {
        return lifeSuggestion;
    }

    public void setLifeSuggestion(LifeSuggestion lifeSuggestion) {
        this.lifeSuggestion = lifeSuggestion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.basicInfo, flags);
        dest.writeParcelable(this.currentInfo, flags);
        dest.writeTypedList(this.hourlyForecastList);
        dest.writeTypedList(this.dailyForecastList);
        dest.writeParcelable(this.airQuality, flags);
        dest.writeParcelable(this.lifeSuggestion, flags);
    }

    protected Weather(Parcel in) {
        this.basicInfo = in.readParcelable(BasicInfo.class.getClassLoader());
        this.currentInfo = in.readParcelable(CurrentInfo.class.getClassLoader());
        this.hourlyForecastList = in.createTypedArrayList(HourlyForecast.CREATOR);
        this.dailyForecastList = in.createTypedArrayList(DailyForecast.CREATOR);
        this.airQuality = in.readParcelable(AirQuality.class.getClassLoader());
        this.lifeSuggestion = in.readParcelable(LifeSuggestion.class.getClassLoader());
    }

    public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel source) {
            return new Weather(source);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };
}
