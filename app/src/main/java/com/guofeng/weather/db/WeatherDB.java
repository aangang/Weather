package com.guofeng.weather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.guofeng.weather.model.City;
import com.guofeng.weather.util.C;

import java.util.ArrayList;
import java.util.List;


public class WeatherDB {


    private static WeatherDB weatherDB;//单例对象
    private SQLiteDatabase mSQLiteDatabase; //数据库处理对象

    private WeatherDB(Context context) {
        //创建一个WeatherOpenHelper对象
        WeatherOpenHelper weatherOpenHelper =
                new WeatherOpenHelper(context, C.DB_NAME, null, C.DB_VERSION);
        //获取SQLiteDatabase对象，通过该对象可以对数据库进行读写操作；
        mSQLiteDatabase = weatherOpenHelper.getWritableDatabase();
    }

    //来个单例
    public static WeatherDB getInstance(Context context) {
        if (weatherDB == null) {
            weatherDB = new WeatherDB(context);
        }
        return weatherDB;
    }
    //------------------------------------------------------------------

    //保存一个城市对象数据
    public void saveCity(City city) {
        if (city != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("CITY_NAME_EN", city.getCity_name_en());
            contentValues.put("CITY_NAME_CH", city.getCity_name_ch());
            contentValues.put("CITY_CODE", city.getCity_code());
            mSQLiteDatabase.insert("CITY", null, contentValues);
        }
    }

    //获取所有的城市
    public List<City> loadCities() {
        List<City> cities = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query("CITY", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId( cursor.getInt(cursor.getColumnIndex("ID")) );
                city.setCity_name_en( cursor.getString(cursor.getColumnIndex("CITY_NAME_EN")) );
                city.setCity_name_ch( cursor.getString(cursor.getColumnIndex("CITY_NAME_CH")) );
                city.setCity_code( cursor.getString(cursor.getColumnIndex("CITY_CODE")) );
                cities.add(city);
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();
        return cities;
    }

    //根据名称获取某一个或多个匹配的城市
    public List<City> loadCitiesByName(String name) {
        List<City> cities = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query(
                "CITY",
                null,
                "CITY_NAME_CH like ?",
                new String[]{name + "%"},
                null,
                null,
                "CITY_CODE"
        );
        while (cursor.moveToNext()) {
            City city = new City();
            city.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            city.setCity_name_en(cursor.getString(cursor.getColumnIndex("CITY_NAME_EN")));
            city.setCity_name_ch(cursor.getString(cursor.getColumnIndex("CITY_NAME_CH")));
            city.setCity_code(cursor.getString(cursor.getColumnIndex("CITY_CODE")));
            cities.add(city);
        }
        if (cursor != null)
            cursor.close();
        return cities;
    }

    //检查是否首次安装（0-是 , 1-否）
    public int checkDataState() {
        int data_state = -1;
        Cursor cursor = mSQLiteDatabase.query("data_state", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                data_state = cursor.getInt(cursor.getColumnIndex("STATE"));
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();
        return data_state;
    }

    //更新状态为已有数据
    public void updateDataState() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", 1);
        mSQLiteDatabase.update("data_state", contentValues, null, null);
    }
}
