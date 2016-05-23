package com.zack.enderweather.bean;

import android.os.Parcel;
import android.os.Parcelable;

/** 空气质量指数 */
public class AirQuality implements Parcelable {

    private String cityId;
    private int aqi, pm10, pm25;

    public AirQuality(String cityId) {
        this.cityId = cityId;
    }

    public AirQuality(String cityId, int aqi, int pm10, int pm25) {
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

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public int getPm10() {
        return pm10;
    }

    public void setPm10(int pm10) {
        this.pm10 = pm10;
    }

    public int getPm25() {
        return pm25;
    }

    public void setPm25(int pm25) {
        this.pm25 = pm25;
    }

    public void setExtraValues(int aqi, int pm10, int pm25) {
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
        dest.writeInt(this.aqi);
        dest.writeInt(this.pm10);
        dest.writeInt(this.pm25);
    }

    protected AirQuality(Parcel in) {
        this.cityId = in.readString();
        this.aqi = in.readInt();
        this.pm10 = in.readInt();
        this.pm25 = in.readInt();
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
