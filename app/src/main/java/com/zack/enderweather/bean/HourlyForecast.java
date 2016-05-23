package com.zack.enderweather.bean;

import android.os.Parcel;
import android.os.Parcelable;

/** 每小时天气预报 */
public class HourlyForecast implements Parcelable {

    private String cityId;
    private String time;
    private int temperature;
    private int windSpeed;
    private String windScale;
    private int windDeg;
    private String windDirection;
    private int pcpnProb;
    private int humidity;
    private int pressure;

    public HourlyForecast(String cityId) {
        this.cityId = cityId;
    }

    public HourlyForecast(String cityId, String time, int temperature, int windSpeed, String windScale,
                          int windDeg, String windDirection, int pcpnProb, int humidity, int pressure) {
        this.cityId = cityId;
        this.time = time;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.windScale = windScale;
        this.windDeg = windDeg;
        this.windDirection = windDirection;
        this.pcpnProb = pcpnProb;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
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

    public void setExtraValues(String time, int temperature, int windSpeed, String windScale, int windDeg,
                               String windDirection, int pcpnProb, int humidity, int pressure) {
        this.time = time;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.windScale = windScale;
        this.windDeg = windDeg;
        this.windDirection = windDirection;
        this.pcpnProb = pcpnProb;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public void clearExtraValues() {
        this.time = null;
        this.temperature = 0;
        this.windSpeed = 0;
        this.windScale = null;
        this.windDeg = 0;
        this.windDirection = null;
        this.pcpnProb = 0;
        this.humidity = 0;
        this.pressure = 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityId);
        dest.writeString(this.time);
        dest.writeInt(this.temperature);
        dest.writeInt(this.windSpeed);
        dest.writeString(this.windScale);
        dest.writeInt(this.windDeg);
        dest.writeString(this.windDirection);
        dest.writeInt(this.pcpnProb);
        dest.writeInt(this.humidity);
        dest.writeInt(this.pressure);
    }

    protected HourlyForecast(Parcel in) {
        this.cityId = in.readString();
        this.time = in.readString();
        this.temperature = in.readInt();
        this.windSpeed = in.readInt();
        this.windScale = in.readString();
        this.windDeg = in.readInt();
        this.windDirection = in.readString();
        this.pcpnProb = in.readInt();
        this.humidity = in.readInt();
        this.pressure = in.readInt();
    }

    public static final Parcelable.Creator<HourlyForecast> CREATOR = new Parcelable.Creator<HourlyForecast>() {
        @Override
        public HourlyForecast createFromParcel(Parcel source) {
            return new HourlyForecast(source);
        }

        @Override
        public HourlyForecast[] newArray(int size) {
            return new HourlyForecast[size];
        }
    };
}
