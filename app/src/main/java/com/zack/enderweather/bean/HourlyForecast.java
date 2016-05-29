package com.zack.enderweather.bean;

import android.os.Parcel;
import android.os.Parcelable;

/** 每小时天气预报 */
public class HourlyForecast implements Parcelable {

    private String cityId;
    private String time;
    private String temperature;
    private String windSpeed;
    private String windScale;
    private String windDeg;
    private String windDirection;
    private String pcpnProb;
    private String humidity;
    private String pressure;

    public HourlyForecast(String cityId) {
        this.cityId = cityId;
    }

    public HourlyForecast(String cityId, String time, String temperature, String windSpeed, String windScale,
                          String windDeg, String windDirection, String pcpnProb, String humidity, String pressure) {
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

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindScale() {
        return windScale;
    }

    public void setWindScale(String windScale) {
        this.windScale = windScale;
    }

    public String getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(String windDeg) {
        this.windDeg = windDeg;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getPcpnProb() {
        return pcpnProb;
    }

    public void setPcpnProb(String pcpnProb) {
        this.pcpnProb = pcpnProb;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setExtraValues(String time, String temperature, String windSpeed, String windScale, String windDeg,
                               String windDirection, String pcpnProb, String humidity, String pressure) {
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
        this.time = "";
        this.temperature = "";
        this.windSpeed = "";
        this.windScale = "";
        this.windDeg = "";
        this.windDirection = "";
        this.pcpnProb = "";
        this.humidity = "";
        this.pressure = "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityId);
        dest.writeString(this.time);
        dest.writeString(this.temperature);
        dest.writeString(this.windSpeed);
        dest.writeString(this.windScale);
        dest.writeString(this.windDeg);
        dest.writeString(this.windDirection);
        dest.writeString(this.pcpnProb);
        dest.writeString(this.humidity);
        dest.writeString(this.pressure);
    }

    protected HourlyForecast(Parcel in) {
        this.cityId = in.readString();
        this.time = in.readString();
        this.temperature = in.readString();
        this.windSpeed = in.readString();
        this.windScale = in.readString();
        this.windDeg = in.readString();
        this.windDirection = in.readString();
        this.pcpnProb = in.readString();
        this.humidity = in.readString();
        this.pressure = in.readString();
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
