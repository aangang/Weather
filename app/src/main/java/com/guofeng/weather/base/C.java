package com.guofeng.weather.base;

import android.os.Environment;

/**
 * 常量类
 * Created by GUOFENG on 2016/10/9.
 */

public class C {

    public static final String PACKAGE_NAME = "com.guofeng.weather";

    //和风天气 key
    public static final String HEFENG_KEY = "218360f478ee492ebfc959870f332f7c";
    //public static final String HEFENG_KEY = "282f3846df6b41178e4a2218ae083ea7";
    //public static final String HEFENG_KEY = "ef54a96746e2406881660dd7f5e74fff";

    //SQLite
    public static final String DB_NAME = "china_city.db";//数据库名称
    //在手机里存放数据库的位置(/data/data/com.guofeng.weather/ChinaCity.db)
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath()
            + "/" + PACKAGE_NAME;

}
