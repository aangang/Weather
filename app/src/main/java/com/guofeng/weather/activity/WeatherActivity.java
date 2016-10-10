package com.guofeng.weather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.guofeng.weather.R;
import com.guofeng.weather.model.City;
import com.guofeng.weather.model.WeatherBasicInfo;
import com.guofeng.weather.model.WeatherDaliyInfo;
import com.guofeng.weather.model.WeatherHourInfo;
import com.guofeng.weather.service.AutoUpdateService;
import com.guofeng.weather.util.ACache;
import com.guofeng.weather.util.C;
import com.guofeng.weather.util.HttpCallback;
import com.guofeng.weather.util.HttpUtil;
import com.guofeng.weather.util.Utility;

import java.util.ArrayList;

/**
 * Created by GUOFENG on 2016/10/4.
 */
public class WeatherActivity extends Activity {

    public static final int REQUEST_CODE = 1;
    //ASimpleCache 是一个为android制定的轻量级的开源缓存框架
    private ACache aCache;
    private WeatherBasicInfo basicInfo;//当前城市的基本信息对象
    private ArrayList hourInfos;//当前城市的小时天气数据
    private ArrayList daliyInfos;
    private City mCurrentCity = new City();//当前显示的城市对象

    private ProgressDialog mProgressDialog;//进度条
    private Button mChangeCityButton;//改变城市按钮
    private Button mRefreshButton;//刷新按钮
    private TextView mCityName;//城市名称
    private TextView mUpdateTime;//更新时间
    //private TextView mCurrentDate;//当前日期
    private TextView mWeatherDesp;//具体的天气情况
    private TextView mTemp1;//最低温度
    private TextView mTemp2;//最高温度
    private TextView city_qlty;//空气质量
    private TextView now_tmp;//当前温度

    private TextView hour_1_time;
    private TextView hour_2_time;
    private TextView hour_3_time;
    private TextView hour_4_time;
    private TextView hour_5_time;
    private TextView hour_1_tmp;
    private TextView hour_2_tmp;
    private TextView hour_3_tmp;
    private TextView hour_4_tmp;
    private TextView hour_5_tmp;

    private TextView day_1_date;
    private TextView day_1_cond;
    private TextView day_1_tmp_min;
    private TextView day_1_tmp_max;

    private TextView day_2_date;
    private TextView day_2_cond;
    private TextView day_2_tmp_min;
    private TextView day_2_tmp_max;

    private TextView day_3_date;
    private TextView day_3_cond;
    private TextView day_3_tmp_min;
    private TextView day_3_tmp_max;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weather);

        init();  //实例化布局的组件
        btnClickListener();//按钮点击事件
        readyFristData();//准备首次的数据
        startAutoUpdataService();//启动自动更新服务

    }

    private void init() {
        mCityName = (TextView) findViewById(R.id.textView_city_name);
        mUpdateTime = (TextView) findViewById(R.id.textView_publishTime);
        //mCurrentDate = (TextView) findViewById(R.id.textView_current_date);
        mWeatherDesp = (TextView) findViewById(R.id.textView_weather_desp);
        mTemp1 = (TextView) findViewById(R.id.textView_temp1);
        mTemp2 = (TextView) findViewById(R.id.textView_temp2);
        city_qlty = (TextView) findViewById(R.id.textView_weather_city_qlty);
        now_tmp = (TextView) findViewById(R.id.now_tmp);
        mChangeCityButton = (Button) findViewById(R.id.button_changeCity);
        mRefreshButton = (Button) findViewById(R.id.button_refresh);

        hour_1_time = (TextView) findViewById(R.id.hour_1_time);
        hour_2_time = (TextView) findViewById(R.id.hour_2_time);
        hour_3_time = (TextView) findViewById(R.id.hour_3_time);
        hour_4_time = (TextView) findViewById(R.id.hour_4_time);
        hour_5_time = (TextView) findViewById(R.id.hour_5_time);

        hour_1_tmp = (TextView) findViewById(R.id.hour_1_tmp);
        hour_2_tmp = (TextView) findViewById(R.id.hour_2_tmp);
        hour_3_tmp = (TextView) findViewById(R.id.hour_3_tmp);
        hour_4_tmp = (TextView) findViewById(R.id.hour_4_tmp);
        hour_5_tmp = (TextView) findViewById(R.id.hour_5_tmp);

        day_1_date = (TextView) findViewById(R.id.day_1_date);
        day_1_cond = (TextView) findViewById(R.id.day_1_cond);
        day_1_tmp_min = (TextView) findViewById(R.id.day_1_tmp_min);
        day_1_tmp_max = (TextView) findViewById(R.id.day_1_tmp_max);

        day_2_date = (TextView) findViewById(R.id.day_2_date);
        day_2_cond = (TextView) findViewById(R.id.day_2_cond);
        day_2_tmp_min = (TextView) findViewById(R.id.day_2_tmp_min);
        day_2_tmp_max = (TextView) findViewById(R.id.day_2_tmp_max);

        day_3_date = (TextView) findViewById(R.id.day_3_date);
        day_3_cond = (TextView) findViewById(R.id.day_3_cond);
        day_3_tmp_min = (TextView) findViewById(R.id.day_3_tmp_min);
        day_3_tmp_max = (TextView) findViewById(R.id.day_3_tmp_max);

        aCache = ACache.get(this);
    }

    private void btnClickListener() {

        //改变城市按钮
        mChangeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动CityChooseActivity
                Intent intent = new Intent(WeatherActivity.this, CityChooseActivity.class);
                // 以返回结果的方式启动
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        //刷新按钮
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWeatherFromServer();//从服务器更新
            }
        });

    }

    private void readyFristData() {
        //首次安装，判断本地存储有无数据，如果没有，城市设置为北京，然后获取数据
        if ((ArrayList) aCache.getAsObject("basicInfos") == null
                || (ArrayList)aCache.getAsObject("hourInfo") == null
                || (ArrayList)aCache.getAsObject("daliyInfo") == null
                )
        {
            mCurrentCity.setCity_code("CN101010100");
            updateWeatherFromServer();
        } else {
            //从本地取数据，也就是上次访问的城市，先确定这个
            loadWeatherData();
        }
    }


    //刷新各组件数据的封装
    private void loadWeatherData() {

        basicInfo = (WeatherBasicInfo) aCache.getAsObject("basicInfo");
        hourInfos = (ArrayList) aCache.getAsObject("hourInfos");
        daliyInfos = (ArrayList) aCache.getAsObject("daliyInfos");


        mCurrentCity.setCity_name_ch(basicInfo.getCityName());
        mCurrentCity.setCity_code(basicInfo.getCityId());

        //basic
        mCityName.setText(basicInfo.getCityName());
        mUpdateTime.setText(basicInfo.getUpdateTime());
        //mCurrentDate.setText(current_data);
        city_qlty.setText(basicInfo.getQlty());
        now_tmp.setText(basicInfo.getNowTmp() + "°");
        if (basicInfo.getD().equals(basicInfo.getN())) {
            mWeatherDesp.setText(basicInfo.getD());
        } else {
            mWeatherDesp.setText(basicInfo.getD() + "转" + basicInfo.getN());
        }
        mTemp1.setText(basicInfo.getMin());
        mTemp2.setText(basicInfo.getMax());

        for (int i = 0; i < daliyInfos.size(); i++) {
            WeatherDaliyInfo daliyInfo = (WeatherDaliyInfo) daliyInfos.get(i);
            if (i == 1) {
                day_1_date.setText(daliyInfo.getDaliyDate());
                day_1_cond.setText(daliyInfo.getD());
                day_1_tmp_max.setText(daliyInfo.getDaliyMax());
                day_1_tmp_min.setText(daliyInfo.getDaliyMin());
            }
            if (i == 2) {
                day_2_date.setText(daliyInfo.getDaliyDate());
                day_2_cond.setText(daliyInfo.getD());
                day_2_tmp_max.setText(daliyInfo.getDaliyMax());
                day_2_tmp_min.setText(daliyInfo.getDaliyMin());
            }
            if (i == 3) {
                day_3_date.setText(daliyInfo.getDaliyDate());
                day_3_cond.setText(daliyInfo.getD());
                day_3_tmp_max.setText(daliyInfo.getDaliyMax());
                day_3_tmp_min.setText(daliyInfo.getDaliyMin());
            }
        }

        //hour
        for (int i = 0; i < hourInfos.size(); i++) {
            WeatherHourInfo hourInfo = (WeatherHourInfo) hourInfos.get(i);
            if (i == 0) {
                hour_1_time.setText(hourInfo.getHour());
                hour_1_tmp.setText(hourInfo.getTmp());
            }
            if (i == 1) {
                hour_2_time.setText(hourInfo.getHour());
                hour_2_tmp.setText(hourInfo.getTmp());
            }
            if (i == 2) {
                hour_4_tmp.setText(hourInfo.getTmp());
                hour_4_time.setText(hourInfo.getHour());

            }
            if (i == 3) {
                hour_3_time.setText(hourInfo.getHour());
                hour_3_tmp.setText(hourInfo.getTmp());
            }
            if (i == 4) {
                hour_5_time.setText(hourInfo.getHour());
                hour_5_tmp.setText(hourInfo.getTmp());
            }
        }
    }

    private void startAutoUpdataService() {
        //启动自动更新服务（不过我这里没怎么使用到自动更新，我这里都是打开后实时更新的，可以打开后不从服务器更新，只从本地获取）
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }


    //从服务器更新数据（CityChooseActivity中有相似方法）
    private void updateWeatherFromServer() {
        String address = "https://api.heweather.com/x3/weather?cityid="
                + mCurrentCity.getCity_code()
                + "&key=" + C.HEFENG_KEY;

        //展示进度条
        showProgressDialog();

        HttpUtil.sendHttpRequest(address, new HttpCallback() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Utility.handleWeatherResponse(response, aCache)) {
                            loadWeatherData();
                            closeProgressDialog(); //关闭进度条
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog(); //关闭进度条
                        Toast.makeText(WeatherActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                loadWeatherData();
            }
        }
    }

    private void showProgressDialog() {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("正在更新数据...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    private void closeProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

}
