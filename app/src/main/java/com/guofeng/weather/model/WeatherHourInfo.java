package com.guofeng.weather.model;

import java.io.Serializable;

/**
 * Created by GUOFENG on 2016/10/9.
 */

public class WeatherHourInfo implements Serializable {
    private String hour;
    private String tmp;

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }


}
