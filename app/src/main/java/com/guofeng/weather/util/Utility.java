package com.guofeng.weather.util;

import android.text.TextUtils;
import android.util.Log;

import com.guofeng.weather.db.WeatherDB;
import com.guofeng.weather.model.City;
import com.guofeng.weather.model.WeatherBasicInfo;
import com.guofeng.weather.model.WeatherDaliyInfo;
import com.guofeng.weather.model.WeatherHourInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Utility {

    //解析从服务器获取的数据
    public synchronized static boolean handleCityResponse(WeatherDB maoWeatherDB, String response) {

        if (!TextUtils.isEmpty(response)) {
            //Log.e("JSon", response);
            try {
                //城市信息JSON比较简单，这里不做详细的解析分析
                JSONArray jsonArray = new JSONObject(response).getJSONArray("city_info");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject city_info = jsonArray.getJSONObject(i);
                    City city = new City();
                    String city_name_ch = city_info.getString("city");
                    String city_name_en = "";
                    String city_code = city_info.getString("id");
                    city.setCity_code(city_code);
                    city.setCity_name_en(city_name_en);
                    city.setCity_name_ch(city_name_ch);
                    maoWeatherDB.saveCity(city);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    //处理从服务器返回的天气信息,返回的数据是比较复杂的JSON数据
    public synchronized static boolean handleWeatherResponse(String response, ACache aCache) {
        if (!TextUtils.isEmpty(response)) {
            try {
                //cityName,cityId,updateTime,qlty,nowTmp,nowDate,min,max,d,n
                WeatherBasicInfo basicInfo = new WeatherBasicInfo();
                ArrayList hourInfos = new ArrayList<WeatherHourInfo>();
                ArrayList daliyInfos = new ArrayList<WeatherDaliyInfo>();

                //1.
                //先把JSON数据加载成数组，
                //因为根部HeWeather data service 3.0后面是[符号，说明是以数组形式存放，只是这个数组里面只有一个元素。
                JSONArray jsonArray = new JSONObject(response).getJSONArray("HeWeather data service 3.0");
                //既然知道这个数组里面只有一个元素，所以我们直接取出第一个元素为JSONObject。
                JSONObject weather_info_all = jsonArray.getJSONObject(0);

                //2.
                //首先，我们看到城市名称和数据更新时间是在basic下面，所以可以直接获取basic的所有数据
                JSONObject weather_info_basic = weather_info_all.getJSONObject("basic");
                //然后，我们直接通过名称获取到city、id信息
                basicInfo.setCityName(weather_info_basic.getString("city"));
                basicInfo.setCityId(weather_info_basic.getString("id"));

                //但是，更新时间不能获直接获取，因为这里update后面是｛｝，表明是一个对象，
                //所以先根据名称获取这个对象
                JSONObject weather_info_basic_update = weather_info_basic.getJSONObject("update");
                //然后再根据这个对象获取名称是loc的数据信息
                basicInfo.setUpdateTime(weather_info_basic_update.getString("loc"));
                //空气质量
                JSONObject weather_info_aqi = weather_info_all.getJSONObject("aqi");
                JSONObject weather_info_aqi_city = weather_info_aqi.getJSONObject("city");
                basicInfo.setQlty(weather_info_aqi_city.getString("qlty"));
                //当前温度
                JSONObject weather_info_now = weather_info_all.getJSONObject("now");
                basicInfo.setNowTmp(weather_info_now.getString("tmp"));

                JSONArray weather_info_hourly_forecast = weather_info_all.getJSONArray("hourly_forecast");
                Log.e("GGGGGG1", weather_info_hourly_forecast.toString());
                for (int i = 0; i < weather_info_hourly_forecast.length(); i++) {
                    JSONObject weather_info_hourly_get = (JSONObject) weather_info_hourly_forecast.get(i);
                    WeatherHourInfo hourInfo = new WeatherHourInfo();
                    hourInfo.setHour(weather_info_hourly_get.getString("date"));
                    hourInfo.setTmp(weather_info_hourly_get.getString("tmp"));
                    hourInfos.add(hourInfo);
                }


                //3.
                //发现关于天气的所有信息都是在daily_forecast名称下面，发现daily_forecast后面是[符号，说明这也是一个JSON数组
                //所以先根据名称获取JSONArray对象
                JSONArray weather_info_daily_forecast = weather_info_all.getJSONArray("daily_forecast");
                //我们发现，[]里面是由很多个像下面这样的元素组成的
                //第一个元素是当前的日期相关的天气数据，目前我们只需要第一个，并且获取出来的是一个JSONObject
                JSONObject weather_info_now_forecast = weather_info_daily_forecast.getJSONObject(0);
                //当前日期date是可以直接获取的，因为date后面是没有｛｝的
                basicInfo.setNowDate(weather_info_now_forecast.getString("date"));

                //tmp节点是当前最低和最高的温度，说明这是一个JSONObject
                JSONObject weather_info_now_forecast_tmp = weather_info_now_forecast.getJSONObject("tmp");
                basicInfo.setMin(weather_info_now_forecast_tmp.getString("min"));
                basicInfo.setMax(weather_info_now_forecast_tmp.getString("max"));

                //cond是当前的实际天气描述，获取方法和tmp是一样的
                JSONObject weather_info_now_forecast_cond = weather_info_now_forecast.getJSONObject("cond");
                basicInfo.setD(weather_info_now_forecast_cond.getString("txt_d"));//天气情况前
                basicInfo.setN(weather_info_now_forecast_cond.getString("txt_n"));//天气情况后


                Log.e("GGGGGG2", weather_info_daily_forecast.toString());
                for (int i = 0; i < weather_info_daily_forecast.length(); i++) {
                    JSONObject weather_info_daliy_get = (JSONObject) weather_info_daily_forecast.get(i);
                    WeatherDaliyInfo daliyInfo = new WeatherDaliyInfo();
                    daliyInfo.setDaliyDate(weather_info_daliy_get.getString("date"));

                    JSONObject weather_info_daliy_tmp=weather_info_daliy_get.getJSONObject("tmp");
                    daliyInfo.setDaliyMax(weather_info_daliy_tmp.getString("max"));
                    daliyInfo.setDaliyMin(weather_info_daliy_tmp.getString("min"));

                    JSONObject weather_info_daliy_cond=weather_info_daliy_get.getJSONObject("cond");
                    daliyInfo.setD(weather_info_daliy_cond.getString("txt_d"));
                    daliyInfo.setN(weather_info_daliy_cond.getString("txt_n"));
                    daliyInfos.add(daliyInfo);

                }
                //4.
                aCache.put("basicInfo", basicInfo, ACache.TIME_DAY);
                aCache.put("hourInfos", hourInfos, ACache.TIME_HOUR);
                aCache.put("daliyInfos", daliyInfos, ACache.TIME_HOUR);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
