package com.guofeng.weather.util.net;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.guofeng.weather.db.WeatherDB;
import com.guofeng.weather.model.City;
import com.guofeng.weather.model.WeatherInfo;
import com.guofeng.weather.util.ACache;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 解析从服务器获取的数据
 */
public class Utility {

    public synchronized static boolean handleCityResponse(WeatherDB weatherDB, String response) {

        if (!TextUtils.isEmpty(response)) {
           // Log.e("JSon", response);
            String city_name;
            String city_code;
            try {
                //城市信息JSON比较简单，这里不做详细的解析分析
                JSONArray jsonArray = new JSONObject(response).getJSONArray("city_info");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject city_info = jsonArray.getJSONObject(i);
                    city_name = city_info.getString("city");
                    city_code = city_info.getString("id");
                    City city = new City();
                    city.setCity_code(city_code);
                    city.setCity_name(city_name);
                    weatherDB.saveCity(city);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    //处理从服务器返回的复杂的JSON数据
    public synchronized static boolean handleWeatherResponse(StringBuilder response, ACache aCache) {
        if (!TextUtils.isEmpty(response)) {
            String info = response.deleteCharAt(11).deleteCharAt(15).delete(22, 26).toString();
            Gson myGson = new Gson();
            WeatherInfo bean = myGson.fromJson(info, WeatherInfo.class);
            aCache.put("tmpWeatherInfo", bean, ACache.TIME_DAY);
            return true;
        }
        return false;
    }
}
