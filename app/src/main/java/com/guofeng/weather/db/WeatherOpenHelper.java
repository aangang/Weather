package com.guofeng.weather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 访问SQLite的助手类
 */

public class WeatherOpenHelper extends SQLiteOpenHelper {

    //创建 城市 表
    private static final String CREATE_CITY = "CREATE TABLE CITY(ID INTEGER PRIMARY KEY,CITY_NAME TEXT,CITY_CODE TEXT)";
    //创建有无数据状态表
    private static final String DATA_STATE = "CREATE TABLE DATA_STATE(STATE INTEGER PRIMARY KEY)";
    //更新状态表 数据为0表示暂无数据
    private static final String INSERT_DATA_STATE = "INSERT INTO DATA_STATE VALUES(0)";

    //在SQLiteOepnHelper的子类当中，必须有该构造函数
    public WeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //在第一次创建数据库的时候执行,实际上在第一次得到SQLiteDatabse对象的时候，才会调用这个方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        //execSQL函数用于执行SQL语句
        db.execSQL(CREATE_CITY);
        db.execSQL(DATA_STATE);
        db.execSQL(INSERT_DATA_STATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
