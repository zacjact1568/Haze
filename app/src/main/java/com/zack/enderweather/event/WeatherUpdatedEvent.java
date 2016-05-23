package com.zack.enderweather.event;

public class WeatherUpdatedEvent {

    public String cityId;

    public WeatherUpdatedEvent(String cityId) {
        this.cityId = cityId;
    }
}
