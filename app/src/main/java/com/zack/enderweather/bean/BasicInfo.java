package com.zack.enderweather.bean;

import android.os.Parcel;
import android.os.Parcelable;

/** 基本信息 */
public class BasicInfo implements Parcelable {

    private String cityId, cityName, updateTime;

    public BasicInfo(String cityId, String cityName) {
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public BasicInfo(String cityId, String cityName, String updateTime) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.updateTime = updateTime;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setExtraValues(String cityName, String updateTime) {
        this.cityName = cityName;
        this.updateTime = updateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityId);
        dest.writeString(this.cityName);
        dest.writeString(this.updateTime);
    }

    protected BasicInfo(Parcel in) {
        this.cityId = in.readString();
        this.cityName = in.readString();
        this.updateTime = in.readString();
    }

    public static final Parcelable.Creator<BasicInfo> CREATOR = new Parcelable.Creator<BasicInfo>() {
        @Override
        public BasicInfo createFromParcel(Parcel source) {
            return new BasicInfo(source);
        }

        @Override
        public BasicInfo[] newArray(int size) {
            return new BasicInfo[size];
        }
    };
}
