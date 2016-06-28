package com.zack.enderweather.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/** 实况天气 */
public class CurrentInfo implements Parcelable {

    private String cityId;
    private String condition;
    private String temperature;
    private String sensibleTemp;
    private String humidity;
    private String precipitation;
    private String pressure;
    private String visibility;
    private String windSpeed;
    private String windScale;
    private String windDeg;
    private String windDirection;

    public CurrentInfo(String cityId) {
        this.cityId = cityId;
        setEmptyValues();
    }

    public CurrentInfo(String cityId, String condition, String temperature, String sensibleTemp, String humidity,
                       String precipitation, String pressure, String visibility, String windSpeed, String windScale,
                       String windDeg, String windDirection) {
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

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getSensibleTemp() {
        return sensibleTemp;
    }

    public void setSensibleTemp(String sensibleTemp) {
        this.sensibleTemp = sensibleTemp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
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

    public void setExtraValues(String condition, String temperature, String sensibleTemp, String humidity,
                               String precipitation, String pressure, String visibility, String windSpeed,
                               String windScale, String windDeg, String windDirection) {
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

    public void setEmptyValues() {
        this.condition = "";
        this.temperature = "";
        this.sensibleTemp = "";
        this.humidity = "";
        this.precipitation = "";
        this.pressure = "";
        this.visibility = "";
        this.windSpeed = "";
        this.windScale = "";
        this.windDeg = "";
        this.windDirection = "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityId);
        dest.writeString(this.condition);
        dest.writeString(this.temperature);
        dest.writeString(this.sensibleTemp);
        dest.writeString(this.humidity);
        dest.writeString(this.precipitation);
        dest.writeString(this.pressure);
        dest.writeString(this.visibility);
        dest.writeString(this.windSpeed);
        dest.writeString(this.windScale);
        dest.writeString(this.windDeg);
        dest.writeString(this.windDirection);
    }

    protected CurrentInfo(Parcel in) {
        this.cityId = in.readString();
        this.condition = in.readString();
        this.temperature = in.readString();
        this.sensibleTemp = in.readString();
        this.humidity = in.readString();
        this.precipitation = in.readString();
        this.pressure = in.readString();
        this.visibility = in.readString();
        this.windSpeed = in.readString();
        this.windScale = in.readString();
        this.windDeg = in.readString();
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
