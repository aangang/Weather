package com.guofeng.weather.model;

import java.io.Serializable;

/**
 * Created by GUOFENG on 2016/10/9.
 */

public class WeatherDaliyInfo implements Serializable {
    private String daliyDate;
    private String daliyMax;
    private String daliyMin;
    private String d;
    private String n;

    public String getDaliyDate() {
        return daliyDate;
    }

    public void setDaliyDate(String daliyDate) {
        this.daliyDate = daliyDate;
    }

    public String getDaliyMax() {
        return daliyMax;
    }

    public void setDaliyMax(String daliyMax) {
        this.daliyMax = daliyMax;
    }

    public String getDaliyMin() {
        return daliyMin;
    }

    public void setDaliyMin(String daliyMin) {
        this.daliyMin = daliyMin;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }
}
