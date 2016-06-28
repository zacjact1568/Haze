package com.zack.enderweather.model.bean;

public class City {

    private String cityId, cityName, prefName, provName;

    public City(String cityId, String cityName, String prefName, String provName) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.prefName = prefName;
        this.provName = provName;
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

    public String getPrefName() {
        return prefName;
    }

    public void setPrefName(String prefName) {
        this.prefName = prefName;
    }

    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }
}
