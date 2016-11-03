package com.guofeng.weather.base;

import android.os.Environment;

/**
 * 常量类
 * Created by GUOFENG on 2016/10/9.
 */

public class C {

    public static final String PACKAGE_NAME = "com.guofeng.weather";

    // 和风天气 key
    public static final String HEFENG_KEY = "928ac8edfbb74d62bb834e9d7a4f9ac7";

    //SQLite
    public static final String DB_NAME = "china_city.db";//数据库名称
    //在手机里存放数据库的位置(/data/data/com.guofeng.weather/ChinaCity.db)
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath()
            + "/" + PACKAGE_NAME;

}
