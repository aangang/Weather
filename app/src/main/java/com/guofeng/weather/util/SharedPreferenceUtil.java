package com.guofeng.weather.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.guofeng.weather.base.BaseApplication;

/**
 * 设置SP相关
 */
public class SharedPreferenceUtil {

    public static final String CITY_NAME = "城市";//选择城市
    public static final String CITY_ID = "城市ID";
    public static final String HOUR = "current_hour";//当前小时
    public static final String CHANGE_ICONS = "change_icons";//切换图标
    public static final String AUTO_UPDATE = "change_update_time"; //自动更新时长
    private SharedPreferences mySP;

    public static SharedPreferenceUtil getInstance() {
        return SPHolder.sInstance;
    }

    private static class SPHolder {
        private static final SharedPreferenceUtil sInstance = new SharedPreferenceUtil();
    }

    private SharedPreferenceUtil() {
        mySP = BaseApplication.getMyAppContext().getSharedPreferences("MySetting", Context.MODE_PRIVATE);
    }

    public SharedPreferenceUtil putInt(String key, int value) {
        mySP.edit().putInt(key, value).apply();
        return this;
    }

    public int getInt(String key, int defValue) {
        return mySP.getInt(key, defValue);
    }

    public SharedPreferenceUtil putString(String key, String value) {
        mySP.edit().putString(key, value).apply();
        return this;
    }

    public String getString(String key, String defValue) {
        return mySP.getString(key, defValue);
    }

    public SharedPreferenceUtil putBoolean(String key, boolean value) {
        mySP.edit().putBoolean(key, value).apply();
        return this;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mySP.getBoolean(key, defValue);
    }

    // 设置当前小时
    public void setCurrentHour(int h) {
        mySP.edit().putInt(HOUR, h).apply();
    }

    public int getCurrentHour() {
        return mySP.getInt(HOUR, 0);
    }

    // 图标种类相关
    public void setIconType(int type) {
        mySP.edit().putInt(CHANGE_ICONS, type).apply();
    }

    public int getIconType() {
        return mySP.getInt(CHANGE_ICONS, 0);
    }

    // 自动更新时间 hours
    public void setAutoUpdate(int t) {
        mySP.edit().putInt(AUTO_UPDATE, t).apply();
    }

    public int getAutoUpdate() {
        return mySP.getInt(AUTO_UPDATE, 3);
    }

    //当前城市
    public void setCityName(String name) {
        mySP.edit().putString(CITY_NAME, name).apply();
    }

    public String getCityName() {
        return mySP.getString(CITY_NAME, "北京");
    }

    //当前城市ID
    public void setCityId(String is) {
        mySP.edit().putString(CITY_ID, is).apply();
    }

    public String getCityID() {
        return mySP.getString(CITY_ID, "CN101010100");
    }


}
