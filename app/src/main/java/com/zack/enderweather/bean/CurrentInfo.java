package com.zack.enderweather.bean;

import android.os.Parcel;
import android.os.Parcelable;

/** 实况天气 */
public class CurrentInfo implements Parcelable {

    private String cityId;
    private String condition;
    private int temperature;
    private int sensibleTemp;
    private int humidity;
    private int precipitation;
    private int pressure;
    private int visibility;
    private int windSpeed;
    private String windScale;
    private int windDeg;
    private String windDirection;

    public CurrentInfo(String cityId) {
        this.cityId = cityId;
    }

    public CurrentInfo(String cityId, String condition, int temperature, int sensibleTemp, int humidity,
                       int precipitation, int pressure, int visibility, int windSpeed, String windScale,
                       int windDeg, String windDirection) {
        this.cityId = cityId;
        this.condition = condition;
        this.temperature = temperature;
        this.sensibleTemp = sensibleTemp;
        this.humidity = humidity;
        this.precipitation = precipitation;
        this.pressure = pressure;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.windScale = windScale;
        this.windDeg = windDeg;
        this.windDirection = windDirection;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getSensibleTemp() {
        return sensibleTemp;
    }

    public void setSensibleTemp(int sensibleTemp) {
        this.sensibleTemp = sensibleTemp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(int precipitation) {
        this.precipitation = precipitation;
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

    public void setExtraValues(String condition, int temperature, int sensibleTemp, int humidity,
                               int precipitation, int pressure, int visibility, int windSpeed,
                               String windScale, int windDeg, String windDirection) {
        this.condition = condition;
        this.temperature = temperature;
        this.sensibleTemp = sensibleTemp;
        this.humidity = humidity;
        this.precipitation = precipitation;
        this.pressure = pressure;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.windScale = windScale;
        this.windDeg = windDeg;
        this.windDirection = windDirection;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityId);
        dest.writeString(this.condition);
        dest.writeInt(this.temperature);
        dest.writeInt(this.sensibleTemp);
        dest.writeInt(this.humidity);
        dest.writeInt(this.precipitation);
        dest.writeInt(this.pressure);
        dest.writeInt(this.visibility);
        dest.writeInt(this.windSpeed);
        dest.writeString(this.windScale);
        dest.writeInt(this.windDeg);
        dest.writeString(this.windDirection);
    }

    protected CurrentInfo(Parcel in) {
        this.cityId = in.readString();
        this.condition = in.readString();
        this.temperature = in.readInt();
        this.sensibleTemp = in.readInt();
        this.humidity = in.readInt();
        this.precipitation = in.readInt();
        this.pressure = in.readInt();
        this.visibility = in.readInt();
        this.windSpeed = in.readInt();
        this.windScale = in.readString();
        this.windDeg = in.readInt();
        this.windDirection = in.readString();
    }

    public static final Parcelable.Creator<CurrentInfo> CREATOR = new Parcelable.Creator<CurrentInfo>() {
        @Override
        public CurrentInfo createFromParcel(Parcel source) {
            return new CurrentInfo(source);
        }

        @Override
        public CurrentInfo[] newArray(int size) {
            return new CurrentInfo[size];
        }
    };
}
