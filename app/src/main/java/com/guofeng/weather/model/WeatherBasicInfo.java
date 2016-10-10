package com.guofeng.weather.model;

import java.io.Serializable;

/**
 * Created by GUOFENG on 2016/10/9.
 */

public class WeatherBasicInfo implements Serializable {
    //cityName,cityId,updateTime,qlty,nowTmp,nowDate,min,max,d,n
    private String cityName;
    private String cityId;
    private String updateTime;
    private String qlty;
    private String nowTmp;
    private String nowDate;
    private String min;
    private String max;
    private String d;
    private String n;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getQlty() {
        return qlty;
    }

    public void setQlty(String qlty) {
        this.qlty = qlty;
    }

    public String getNowTmp() {
        return nowTmp;
    }

    public void setNowTmp(String nowTmp) {
        this.nowTmp = nowTmp;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
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
