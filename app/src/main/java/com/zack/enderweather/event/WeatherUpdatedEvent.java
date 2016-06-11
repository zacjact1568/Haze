package com.zack.enderweather.event;

public class WeatherUpdatedEvent {

    public int position;
    public String cityId;
    public boolean isSuc;

    public WeatherUpdatedEvent(int position, String cityId, boolean isSuc) {
        this.position = position;
        this.cityId = cityId;
        this.isSuc = isSuc;
    }
}
