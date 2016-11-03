package com.guofeng.weather.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.guofeng.weather.model.City;
import com.guofeng.weather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作
 * Created by GUOFENG on 2016/11/3.
 */

public class WeatherDBHelper {
    private static String TAG = "WeatherDBHelper";

    public static List<Province> selectProvinces(SQLiteDatabase db) {
        List<Province> provinceList = new ArrayList<>();
        Log.e(TAG, "查询省份");
        Cursor cursor = db.query("T_Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.ProvinceSort = cursor.getInt(cursor.getColumnIndex("ProSort"));
                province.ProvinceName = cursor.getString(cursor.getColumnIndex("ProName"));
                provinceList.add(province);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed())
            cursor.close();
        Log.e(TAG, String.valueOf(provinceList.size()));
        return provinceList;
    }

    public static List<City> selectCities(SQLiteDatabase db, int ProID) {
        List<City> cityList = new ArrayList<>();
        Log.e(TAG, "查询城市");
        Cursor cursor = db.query("T_City", null, "ProID = ?", new String[]{String.valueOf(ProID)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.CityName = cursor.getString(cursor.getColumnIndex("CityName"));
                city.ProvinceID = ProID;
                city.CitySort = cursor.getInt(cursor.getColumnIndex("CitySort"));
                cityList.add(city);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed())
            cursor.close();
        Log.e(TAG, String.valueOf(cityList.size()));
        return cityList;
    }

}
