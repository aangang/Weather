package com.guofeng.weather.model;

/**
 * Created by GUOFENG on 2016/10/29.
 */

public class ChangeCityEvent {
    private String city;
    private boolean isSetting;

    public ChangeCityEvent() {
    }

    public ChangeCityEvent(String city) {
        this.city = city;
    }

    public ChangeCityEvent(boolean isSetting) {
        this.isSetting = isSetting;
    }
}
