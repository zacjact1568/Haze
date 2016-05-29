package com.zack.enderweather.bean;

import android.os.Parcel;
import android.os.Parcelable;

/** 空气质量指数 */
public class AirQuality implements Parcelable {

    private String cityId;
    private String aqi, pm10, pm25;

    public AirQuality(String cityId) {
        this.cityId = cityId;
    }

    public AirQuality(String cityId, String aqi, String pm10, String pm25) {
        this.cityId = cityId;
        this.aqi = aqi;
        this.pm10 = pm10;
        this.pm25 = pm25;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getPm10() {
        return pm10;
    }

    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public void setExtraValues(String aqi, String pm10, String pm25) {
        this.aqi = aqi;
        this.pm10 = pm10;
        this.pm25 = pm25;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityId);
        dest.writeString(this.aqi);
        dest.writeString(this.pm10);
        dest.writeString(this.pm25);
    }

    protected AirQuality(Parcel in) {
        this.cityId = in.readString();
        this.aqi = in.readString();
        this.pm10 = in.readString();
        this.pm25 = in.readString();
    }

    public static final Parcelable.Creator<AirQuality> CREATOR = new Parcelable.Creator<AirQuality>() {
        @Override
        public AirQuality createFromParcel(Parcel source) {
            return new AirQuality(source);
        }

        @Override
        public AirQuality[] newArray(int size) {
            return new AirQuality[size];
        }
    };
}
