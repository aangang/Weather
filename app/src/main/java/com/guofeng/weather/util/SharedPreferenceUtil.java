package com.guofeng.weather.util;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;

import com.guofeng.weather.base.BaseApplication;

import java.util.ArrayList;

/**
 * 设置SP相关
 */
public class SharedPreferenceUtil {

    public static final String CITY_NAME = "location";//选择城市
    public static final String AUTO_UPDATE = "auto_update_time"; //自动更新时长
    public static final String NOTIFICATION_MODEL = "notification_model";//通知栏模式
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

    //----------------------------------------------------------

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

    //自动更新时长
    public void setAutoUpdate(int t) {
        mySP.edit().putInt(AUTO_UPDATE, t).apply();
    }

    public int getAutoUpdate() {
        return mySP.getInt(AUTO_UPDATE, 2);
    }

    //通知栏默认为常驻
    public void setNotificationModel(int t) {
        mySP.edit().putInt(NOTIFICATION_MODEL, t).apply();
    }

    public int getNotificationModel() {
        return mySP.getInt(NOTIFICATION_MODEL, Notification.FLAG_ONGOING_EVENT);
    }

    //定位城市
    public void setCityName(String name) {
        mySP.edit().putString(CITY_NAME, name).apply();
    }

    public String getCityName() {
        return mySP.getString(CITY_NAME, "北京");
    }

    //关注城市列表
    public boolean saveArray(ArrayList<String> arr) {
        mySP.edit().putInt("Status_size", arr.size()).apply(); /*sKey is an array*/

        for (int i = 0; i < arr.size(); i++) {
            mySP.edit().remove("Status_" + i).apply();
            mySP.edit().putString("Status_" + i, arr.get(i)).apply();
        }
        return mySP.edit().commit();
    }

    public ArrayList<String> getArray() {
        ArrayList<String> arr = new ArrayList<>();
        int size = mySP.getInt("Status_size", 0);
        for (int i = 0; i < size; i++) {
            arr.add(mySP.getString("Status_" + i, null));

        }
        return arr;
    }
}
