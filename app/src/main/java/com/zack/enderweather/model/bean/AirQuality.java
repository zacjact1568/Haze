package com.zack.enderweather.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/** 空气质量指数 */
public class AirQuality implements Parcelable {

    private String cityId;
    private String aqi;
    private String co;
    private String no2;
    private String o3;
    private String pm10;
    private String pm25;
    private String qlty;
    private String so2;

    public AirQuality(String cityId) {
        this.cityId = cityId;
        setEmptyValues();
    }

    public AirQuality(String cityId, String aqi, String co, String no2, String o3, String pm10,
                      String pm25, String qlty, String so2) {
        this.cityId = cityId;
        this.aqi = aqi;
        this.co = co;
        this.no2 = no2;
        this.o3 = o3;
        this.pm10 = pm10;
        this.pm25 = pm25;
        this.qlty = qlty;
        this.so2 = so2;
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

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getNo2() {
        return no2;
    }

    public void setNo2(String no2) {
        this.no2 = no2;
    }

    public String getO3() {
        return o3;
    }

    public void setO3(String o3) {
        this.o3 = o3;
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

    public String getQlty() {
        return qlty;
    }

    public void setQlty(String qlty) {
        this.qlty = qlty;
    }

    public String getSo2() {
        return so2;
    }

    public void setSo2(String so2) {
        this.so2 = so2;
    }

    public void setExtraValues(String aqi, String co, String no2, String o3, String pm10, String pm25,
                               String qlty, String so2) {
        this.aqi = aqi;
        this.co = co;
        this.no2 = no2;
        this.o3 = o3;
        this.pm10 = pm10;
        this.pm25 = pm25;
        this.qlty = qlty;
        this.so2 = so2;
    }

    public void setEmptyValues() {
        this.aqi = "";
        this.co = "";
        this.no2 = "";
        this.o3 = "";
        this.pm10 = "";
        this.pm25 = "";
        this.qlty = "";
        this.so2 = "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityId);
        dest.writeString(this.aqi);
        dest.writeString(this.co);
        dest.writeString(this.no2);
        dest.writeString(this.o3);
        dest.writeString(this.pm10);
        dest.writeString(this.pm25);
        dest.writeString(this.qlty);
        dest.writeString(this.so2);
    }

    protected AirQuality(Parcel in) {
        this.cityId = in.readString();
        this.aqi = in.readString();
        this.co = in.readString();
        this.no2 = in.readString();
        this.o3 = in.readString();
        this.pm10 = in.readString();
        this.pm25 = in.readString();
        this.qlty = in.readString();
        this.so2 = in.readString();
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
