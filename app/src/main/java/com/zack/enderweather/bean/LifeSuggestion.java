package com.zack.enderweather.bean;

import android.os.Parcel;
import android.os.Parcelable;

/** 生活建议 */
public class LifeSuggestion implements Parcelable {

    private String cityId;
    private String comfort;
    private String dressing;
    private String uvRay;
    private String carWash;
    private String travel;
    private String flu;
    private String sport;

    public LifeSuggestion(String cityId) {
        this.cityId = cityId;
        setEmptyValues();
    }

    public LifeSuggestion(String cityId, String comfort, String dressing, String uvRay, String carWash,
                          String travel, String flu, String sport) {
        this.cityId = cityId;
        this.comfort = comfort;
        this.dressing = dressing;
        this.uvRay = uvRay;
        this.carWash = carWash;
        this.travel = travel;
        this.flu = flu;
        this.sport = sport;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getComfort() {
        return comfort;
    }

    public void setComfort(String comfort) {
        this.comfort = comfort;
    }

    public String getDressing() {
        return dressing;
    }

    public void setDressing(String dressing) {
        this.dressing = dressing;
    }

    public String getUvRay() {
        return uvRay;
    }

    public void setUvRay(String uvRay) {
        this.uvRay = uvRay;
    }

    public String getCarWash() {
        return carWash;
    }

    public void setCarWash(String carWash) {
        this.carWash = carWash;
    }

    public String getTravel() {
        return travel;
    }

    public void setTravel(String travel) {
        this.travel = travel;
    }

    public String getFlu() {
        return flu;
    }

    public void setFlu(String flu) {
        this.flu = flu;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public void setExtraValues(String comfort, String dressing, String uvRay, String carWash,
                               String travel, String flu, String sport) {
        this.comfort = comfort;
        this.dressing = dressing;
        this.uvRay = uvRay;
        this.carWash = carWash;
        this.travel = travel;
        this.flu = flu;
        this.sport = sport;
    }

    public void setEmptyValues() {
        this.comfort = "";
        this.dressing = "";
        this.uvRay = "";
        this.carWash = "";
        this.travel = "";
        this.flu = "";
        this.sport = "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityId);
        dest.writeString(this.comfort);
        dest.writeString(this.dressing);
        dest.writeString(this.uvRay);
        dest.writeString(this.carWash);
        dest.writeString(this.travel);
        dest.writeString(this.flu);
        dest.writeString(this.sport);
    }

    protected LifeSuggestion(Parcel in) {
        this.cityId = in.readString();
        this.comfort = in.readString();
        this.dressing = in.readString();
        this.uvRay = in.readString();
        this.carWash = in.readString();
        this.travel = in.readString();
        this.flu = in.readString();
        this.sport = in.readString();
    }

    public static final Parcelable.Creator<LifeSuggestion> CREATOR = new Parcelable.Creator<LifeSuggestion>() {
        @Override
        public LifeSuggestion createFromParcel(Parcel source) {
            return new LifeSuggestion(source);
        }

        @Override
        public LifeSuggestion[] newArray(int size) {
            return new LifeSuggestion[size];
        }
    };
}
