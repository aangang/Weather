package com.guofeng.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.guofeng.weather.R;
import com.guofeng.weather.base.C;
import com.guofeng.weather.model.City;
import com.guofeng.weather.model.WeatherInfo;
import com.guofeng.weather.service.AutoUpdateService;
import com.guofeng.weather.util.ACache;
import com.guofeng.weather.util.AMapLocationUtil;
import com.guofeng.weather.util.SharedPreferenceUtil;
import com.guofeng.weather.util.ToastUtil;
import com.guofeng.weather.util.net.HttpCallback;
import com.guofeng.weather.util.net.HttpUtil;
import com.guofeng.weather.util.net.Utility;
import com.guofeng.weather.util.net.VolleyUtil;

/**
 * Created by GUOFENG on 2016/10/4.
 */
public class WeatherActivity extends Activity {

    //ASimpleCache 是一个为android制定的轻量级的开源缓存框架
    private ACache aCache;
    private AMapLocationUtil gaode = new AMapLocationUtil();

    public static final int REQUEST_CODE = 1;
    private City mCurrentCity = new City();//当前显示的城市对象
    private VolleyUtil weatherImg = new VolleyUtil();

    private WeatherInfo bean;
    private Button mChangeCityButton;//改变城市按钮
    private Button mRefreshButton;//刷新按钮
    private TextView mCityName;//城市名称
    private TextView mUpdateTime;//更新时间
    private TextView mWeatherDesp;//具体的天气情况
    private TextView mTemp1;//最低温度
    private TextView mTemp2;//最高温度
    private TextView city_qlty;//空气质量
    private TextView now_tmp;//当前温度


    private TextView day_1_date;
    private ImageView day_1_cond;
    private TextView day_1_tmp_min;
    private TextView day_1_tmp_max;

    private TextView day_2_date;
    private ImageView day_2_cond;
    private TextView day_2_tmp_min;
    private TextView day_2_tmp_max;

    private TextView day_3_date;
    private ImageView day_3_cond;
    private TextView day_3_tmp_min;
    private TextView day_3_tmp_max;

    private TextView day_4_date;
    private ImageView day_4_cond;
    private TextView day_4_tmp_min;
    private TextView day_4_tmp_max;
    private ImageView myimg;

    private ScrollView myScroll;
    private SwipeRefreshLayout mySwipeRefresh;
    private TextView tv_refresh;


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

        day_1_date = (TextView) findViewById(R.id.day_1_date);
        day_1_cond = (ImageView) findViewById(R.id.day_1_cond);
        day_1_tmp_min = (TextView) findViewById(R.id.day_1_tmp_min);
        day_1_tmp_max = (TextView) findViewById(R.id.day_1_tmp_max);

        day_2_date = (TextView) findViewById(R.id.day_2_date);
        day_2_cond = (ImageView) findViewById(R.id.day_2_cond);
        day_2_tmp_min = (TextView) findViewById(R.id.day_2_tmp_min);
        day_2_tmp_max = (TextView) findViewById(R.id.day_2_tmp_max);

        day_3_date = (TextView) findViewById(R.id.day_3_date);
        day_3_cond = (ImageView) findViewById(R.id.day_3_cond);
        day_3_tmp_min = (TextView) findViewById(R.id.day_3_tmp_min);
        day_3_tmp_max = (TextView) findViewById(R.id.day_3_tmp_max);

        day_4_date = (TextView) findViewById(R.id.day_4_date);
        day_4_cond = (ImageView) findViewById(R.id.day_4_cond);
        day_4_tmp_min = (TextView) findViewById(R.id.day_4_tmp_min);
        day_4_tmp_max = (TextView) findViewById(R.id.day_4_tmp_max);


        myimg = (ImageView) findViewById(R.id.myimg);


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
        gaode.start();
        aCache = ACache.get(this);



        //首次安装，判断本地存储有无数据，默认从服务器获取北京数据
        if (aCache.getAsObject("tmpWeatherInfo") == null) {
            Log.e("FFF", "从服务器获取数据！");
            updateWeatherFromServer();
        } else {
            //从本地取数据，也就是上次访问的城市，先确定这个
            bean = (WeatherInfo) aCache.getAsObject("tmpWeatherInfo");
            Log.e("FFF", "从本地获取数据！");

            if (bean.getHeWeatherdataservice().get(0).getStatus().equals("ok")) {
                loadWeatherData();
            } else {
                //ToastUtil.showLongToast("今日API已经用尽调用次数！");
                updateWeatherFromServer();
            }

        }



    }


    //刷新各组件数据的封装
    private void loadWeatherData() {

        bean = (WeatherInfo) aCache.getAsObject("tmpWeatherInfo");
        WeatherInfo.HeWeatherdataserviceBean my
                = bean.getHeWeatherdataservice().get(0);

        mCurrentCity.setCity_name(my.getBasic().getCity());
        mCurrentCity.setCity_code(my.getBasic().getId());

        mCityName.setText(my.getBasic().getCity());
        mUpdateTime.setText(my.getBasic().getUpdate().getLoc());
        city_qlty.setText(my.getAqi().getCity().getQlty());
        now_tmp.setText(my.getNow().getTmp() + "°");
        if (my.getDaily_forecast().get(0).getCond().getTxt_d()
                .equals(my.getDaily_forecast().get(0).getCond().getTxt_n())) {
            mWeatherDesp.setText(my.getDaily_forecast().get(0).getCond().getTxt_d());
        } else {
            mWeatherDesp.setText(my.getDaily_forecast().get(0).getCond().getTxt_d() + "转"
                    + my.getDaily_forecast().get(0).getCond().getTxt_n());
        }
        mTemp1.setText(my.getDaily_forecast().get(0).getTmp().getMin());
        mTemp2.setText(my.getDaily_forecast().get(0).getTmp().getMax());


        for (int i = 0; i < my.getDaily_forecast().size(); i++) {
            if (i == 1) {
                day_1_date.setText(my.getDaily_forecast().get(i).getDate());
                // day_1_cond.setText(my.getDaily_forecast().get(i).getCond().getTxt_d());
                weatherImg.myVolley(my.getDaily_forecast().get(i).getCond().getCode_d(), day_1_cond);
                day_1_tmp_max.setText(my.getDaily_forecast().get(i).getTmp().getMin());
                day_1_tmp_min.setText(my.getDaily_forecast().get(i).getTmp().getMax());
            }
            if (i == 2) {
                day_2_date.setText(my.getDaily_forecast().get(i).getDate());
                weatherImg.myVolley(my.getDaily_forecast().get(i).getCond().getCode_d(), day_2_cond);
                day_2_tmp_max.setText(my.getDaily_forecast().get(i).getTmp().getMin());
                day_2_tmp_min.setText(my.getDaily_forecast().get(i).getTmp().getMax());
            }
            if (i == 3) {
                day_3_date.setText(my.getDaily_forecast().get(i).getDate());
                weatherImg.myVolley(my.getDaily_forecast().get(i).getCond().getCode_d(), day_3_cond);
                day_3_tmp_max.setText(my.getDaily_forecast().get(i).getTmp().getMin());
                day_3_tmp_min.setText(my.getDaily_forecast().get(i).getTmp().getMax());
            }
            if (i == 4) {
                day_4_date.setText(my.getDaily_forecast().get(i).getDate());
                weatherImg.myVolley(my.getDaily_forecast().get(i).getCond().getCode_d(), day_4_cond);
                day_4_tmp_max.setText(my.getDaily_forecast().get(i).getTmp().getMin());
                day_4_tmp_min.setText(my.getDaily_forecast().get(i).getTmp().getMax());
            }
        }

    }

    //启动自动更新服务
    private void startAutoUpdataService() {
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }


    //从服务器更新数据（CityChooseActivity中有相似方法）
    private void updateWeatherFromServer() {
        String address = "https://api.heweather.com/x3/weather?cityid="
                + SharedPreferenceUtil.getInstance().getCityID()
                + "&key=" + C.HEFENG_KEY;

        ToastUtil.showShortToast("正在更新信息");

        HttpUtil.sendHttpRequest(address, new HttpCallback() {
            @Override
            public void onFinish(final StringBuilder response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Utility.handleWeatherResponse(response, aCache)) {
                            loadWeatherData();
                            ToastUtil.showShortToast("更新天气完毕");

                        }
                    }
                });
            }

            @Override
            public void onError(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast("更新天气出错" + e.getMessage());

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
}
