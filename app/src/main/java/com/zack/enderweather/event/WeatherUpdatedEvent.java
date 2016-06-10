package com.zack.enderweather.event;

public class WeatherUpdatedEvent {

    public String cityId;
    public boolean isSuc;

    public WeatherUpdatedEvent(String cityId, boolean isSuc) {
        this.cityId = cityId;
        this.isSuc = isSuc;
    }
}
