package com.zack.enderweather.bean;

import android.os.Parcel;
import android.os.Parcelable;

/** 每日天气预报 */
public class DailyForecast implements Parcelable {

    private String cityId;
    private String date;
    private String sunriseTime;
    private String sunsetTime;
    private int maxTemp;
    private int minTemp;
    private int windSpeed;
    private String windScale;
    private int windDeg;
    private String windDirection;
    private String conditionDay;
    private String conditionNight;
    private int precipitation;
    private int pcpnProb;
    private int humidity;
    private int pressure;
    private int visibility;

    public DailyForecast(String cityId) {
        this.cityId = cityId;
    }

    public DailyForecast(String cityId, String date, String sunriseTime, String sunsetTime, int maxTemp,
                         int minTemp, int windSpeed, String windScale, int windDeg, String windDirection,
                         String conditionDay, String conditionNight, int precipitation, int pcpnProb,
                         int humidity, int pressure, int visibility) {
        this.cityId = cityId;
        this.date = date;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.windSpeed = windSpeed;
        this.windScale = windScale;
        this.windDeg = windDeg;
        this.windDirection = windDirection;
        this.conditionDay = conditionDay;
        this.conditionNight = conditionNight;
        this.precipitation = precipitation;
        this.pcpnProb = pcpnProb;
        this.humidity = humidity;
        this.pressure = pressure;
        this.visibility = visibility;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(String sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public String getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(String sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindScale() {
        return windScale;
    }

    public void setWindScale(String windScale) {
        this.windScale = windScale;
    }

    public int getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(int windDeg) {
        this.windDeg = windDeg;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getConditionDay() {
        return conditionDay;
    }

    public void setConditionDay(String conditionDay) {
        this.conditionDay = conditionDay;
    }

    public String getConditionNight() {
        return conditionNight;
    }

    public void setConditionNight(String conditionNight) {
        this.conditionNight = conditionNight;
    }

    public int getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(int precipitation) {
        this.precipitation = precipitation;
    }

    public int getPcpnProb() {
        return pcpnProb;
    }

    public void setPcpnProb(int pcpnProb) {
        this.pcpnProb = pcpnProb;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public void setExtraValues(String date, String sunriseTime, String sunsetTime, int maxTemp,
                               int minTemp, int windSpeed, String windScale, int windDeg, String windDirection,
                               String conditionDay, String conditionNight, int precipitation, int pcpnProb,
                               int humidity, int pressure, int visibility) {
        this.date = date;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.windSpeed = windSpeed;
        this.windScale = windScale;
        this.windDeg = windDeg;
        this.windDirection = windDirection;
        this.conditionDay = conditionDay;
        this.conditionNight = conditionNight;
        this.precipitation = precipitation;
        this.pcpnProb = pcpnProb;
        this.humidity = humidity;
        this.pressure = pressure;
        this.visibility = visibility;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityId);
        dest.writeString(this.date);
        dest.writeString(this.sunriseTime);
        dest.writeString(this.sunsetTime);
        dest.writeInt(this.maxTemp);
        dest.writeInt(this.minTemp);
        dest.writeInt(this.windSpeed);
        dest.writeString(this.windScale);
        dest.writeInt(this.windDeg);
        dest.writeString(this.windDirection);
        dest.writeString(this.conditionDay);
        dest.writeString(this.conditionNight);
        dest.writeInt(this.precipitation);
        dest.writeInt(this.pcpnProb);
        dest.writeInt(this.humidity);
        dest.writeInt(this.pressure);
        dest.writeInt(this.visibility);
    }

    protected DailyForecast(Parcel in) {
        this.cityId = in.readString();
        this.date = in.readString();
        this.sunriseTime = in.readString();
        this.sunsetTime = in.readString();
        this.maxTemp = in.readInt();
        this.minTemp = in.readInt();
        this.windSpeed = in.readInt();
        this.windScale = in.readString();
        this.windDeg = in.readInt();
        this.windDirection = in.readString();
        this.conditionDay = in.readString();
        this.conditionNight = in.readString();
        this.precipitation = in.readInt();
        this.pcpnProb = in.readInt();
        this.humidity = in.readInt();
        this.pressure = in.readInt();
        this.visibility = in.readInt();
    }

    public static final Parcelable.Creator<DailyForecast> CREATOR = new Parcelable.Creator<DailyForecast>() {
        @Override
        public DailyForecast createFromParcel(Parcel source) {
            return new DailyForecast(source);
        }

        @Override
        public DailyForecast[] newArray(int size) {
            return new DailyForecast[size];
        }
    };
}
