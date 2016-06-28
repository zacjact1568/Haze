package com.zack.enderweather.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/** 每日天气预报 */
public class DailyForecast implements Parcelable {

    private String cityDateId;
    private String date;
    private String sunriseTime;
    private String sunsetTime;
    private String maxTemp;
    private String minTemp;
    private String windSpeed;
    private String windScale;
    private String windDeg;
    private String windDirection;
    private String conditionDay;
    private String conditionNight;
    private String precipitation;
    private String pcpnProb;
    private String humidity;
    private String pressure;
    private String visibility;

    public DailyForecast(String cityDateId) {
        this.cityDateId = cityDateId;
        setEmptyValues();
    }

    public DailyForecast(String cityDateId, String date, String sunriseTime, String sunsetTime, String maxTemp,
                         String minTemp, String windSpeed, String windScale, String windDeg, String windDirection,
                         String conditionDay, String conditionNight, String precipitation, String pcpnProb,
                         String humidity, String pressure, String visibility) {
        this.cityDateId = cityDateId;
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

    public String getCityDateId() {
        return cityDateId;
    }

    public void setCityDateId(String cityDateId) {
        this.cityDateId = cityDateId;
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

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
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

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
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

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public void setExtraValues(String date, String sunriseTime, String sunsetTime, String maxTemp,
                               String minTemp, String windSpeed, String windScale, String windDeg, String windDirection,
                               String conditionDay, String conditionNight, String precipitation, String pcpnProb,
                               String humidity, String pressure, String visibility) {
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

    public void setEmptyValues() {
        this.date = "";
        this.sunriseTime = "";
        this.sunsetTime = "";
        this.maxTemp = "";
        this.minTemp = "";
        this.windSpeed = "";
        this.windScale = "";
        this.windDeg = "";
        this.windDirection = "";
        this.conditionDay = "";
        this.conditionNight = "";
        this.precipitation = "";
        this.pcpnProb = "";
        this.humidity = "";
        this.pressure = "";
        this.visibility = "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityDateId);
        dest.writeString(this.date);
        dest.writeString(this.sunriseTime);
        dest.writeString(this.sunsetTime);
        dest.writeString(this.maxTemp);
        dest.writeString(this.minTemp);
        dest.writeString(this.windSpeed);
        dest.writeString(this.windScale);
        dest.writeString(this.windDeg);
        dest.writeString(this.windDirection);
        dest.writeString(this.conditionDay);
        dest.writeString(this.conditionNight);
        dest.writeString(this.precipitation);
        dest.writeString(this.pcpnProb);
        dest.writeString(this.humidity);
        dest.writeString(this.pressure);
        dest.writeString(this.visibility);
    }

    protected DailyForecast(Parcel in) {
        this.cityDateId = in.readString();
        this.date = in.readString();
        this.sunriseTime = in.readString();
        this.sunsetTime = in.readString();
        this.maxTemp = in.readString();
        this.minTemp = in.readString();
        this.windSpeed = in.readString();
        this.windScale = in.readString();
        this.windDeg = in.readString();
        this.windDirection = in.readString();
        this.conditionDay = in.readString();
        this.conditionNight = in.readString();
        this.precipitation = in.readString();
        this.pcpnProb = in.readString();
        this.humidity = in.readString();
        this.pressure = in.readString();
        this.visibility = in.readString();
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
