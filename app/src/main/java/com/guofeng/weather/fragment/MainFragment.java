package com.guofeng.weather.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.guofeng.weather.R;
import com.guofeng.weather.base.C;
import com.guofeng.weather.model.City;
import com.guofeng.weather.model.WeatherInfo;
import com.guofeng.weather.util.ACache;
import com.guofeng.weather.util.AMapLocationUtil;
import com.guofeng.weather.util.SharedPreferenceUtil;
import com.guofeng.weather.util.ToastUtil;
import com.guofeng.weather.util.net.HttpCallback;
import com.guofeng.weather.util.net.HttpUtil;
import com.guofeng.weather.util.net.Utility;
import com.guofeng.weather.util.net.VolleyUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GUOFENG on 2016/10/22.
 */
public class MainFragment extends Fragment {

    private View view;
    //ASimpleCache 是一个为android制定的轻量级的开源缓存框架
    private ACache aCache;
    private AMapLocationUtil gaode = new AMapLocationUtil();
    public static final int REQUEST_CODE = 1;
    private City mCurrentCity = new City();//当前显示的城市对象
    private VolleyUtil weatherImg = new VolleyUtil();
    private WeatherInfo bean;

    @BindView(R.id.button_changeCity)
    Button mChangeCityButton;//改变城市按钮
    @BindView(R.id.button_refresh)
    Button mRefreshButton;//刷新按钮
    @BindView(R.id.textView_city_name)
    TextView mCityName;//城市名称
    @BindView(R.id.textView_publishTime)
    TextView mUpdateTime;//更新时间
    @BindView(R.id.textView_weather_desp)
    TextView mWeatherDesp;//具体的天气情况
    @BindView(R.id.textView_weather_city_qlty)
    TextView city_qlty;//空气质量
    @BindView(R.id.now_tmp)
    TextView now_tmp;//当前温度

    @BindView(R.id.day_1_date)
    TextView day_1_date;
    @BindView(R.id.day_1_cond)
    ImageView day_1_cond;
    @BindView(R.id.day_1_tmp_min)
    TextView day_1_tmp_min;
    @BindView(R.id.day_1_tmp_max)
    TextView day_1_tmp_max;

    @BindView(R.id.day_2_date)
    TextView day_2_date;
    @BindView(R.id.day_2_cond)
    ImageView day_2_cond;
    @BindView(R.id.day_2_tmp_min)
    TextView day_2_tmp_min;
    @BindView(R.id.day_2_tmp_max)
    TextView day_2_tmp_max;

    @BindView(R.id.day_3_date)
    TextView day_3_date;
    @BindView(R.id.day_3_cond)
    ImageView day_3_cond;
    @BindView(R.id.day_3_tmp_min)
    TextView day_3_tmp_min;
    @BindView(R.id.day_3_tmp_max)
    TextView day_3_tmp_max;

    @BindView(R.id.day_4_date)
    TextView day_4_date;
    @BindView(R.id.day_4_cond)
    ImageView day_4_cond;
    @BindView(R.id.day_4_tmp_min)
    TextView day_4_tmp_min;
    @BindView(R.id.day_4_tmp_max)
    TextView day_4_tmp_max;

    ScrollView myScroll;
    SwipeRefreshLayout mySwipeRefresh;
    TextView tv_refresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main, container, false);
            ButterKnife.bind(this, view);
        }
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnClickListener();//按钮点击事件
        readyFristData();//准备首次的数据
    }


    private void btnClickListener() {
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
        aCache = ACache.get(getActivity());

        //首次安装，判断本地存储有无数据，默认从服务器获取北京数据
        if (aCache.getAsObject("tmpWeatherInfo") == null) {
            Log.e("FFF", "从服务器获取数据！");
            updateWeatherFromServer();
        } else {
            //从本地取数据，也就是上次访问的城市，先确定这个
            bean = (WeatherInfo) aCache.getAsObject("tmpWeatherInfo");
            Log.e("FFF", "从本地获取数据！");

            if (bean.getHeWeatherdataservice().get(0).getStatus().equals("ok")) {
                try {
                    loadWeatherData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //ToastUtil.showLongToast("今日API已经用尽调用次数！");
                updateWeatherFromServer();
            }

        }

    }

    //刷新各组件数据的封装
    private void loadWeatherData() throws Exception {

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
        day_1_tmp_min.setText(my.getDaily_forecast().get(0).getTmp().getMin());
        day_1_tmp_max.setText(my.getDaily_forecast().get(0).getTmp().getMax());


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


    //从服务器更新数据（CityChooseActivity中有相似方法）
    private void updateWeatherFromServer() {
        String address = "https://api.heweather.com/x3/weather?cityid="
                + SharedPreferenceUtil.getInstance().getCityID()
                + "&key=" + C.HEFENG_KEY;

        ToastUtil.showShortToast("正在更新信息");

        HttpUtil.sendHttpRequest(address, new HttpCallback() {
            @Override
            public void onFinish(final StringBuilder response) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (Utility.handleWeatherResponse(response, aCache)) {
//                            try {
//                                loadWeatherData();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            ToastUtil.showShortToast("更新天气完毕");
//
//                        }
//                    }
//                });

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (Utility.handleWeatherResponse(response, aCache)) {
                            myHandler.sendEmptyMessage(1);
                        }
                    }
                }).start();
            }

            @Override
            public void onError(final Exception e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtil.showShortToast("更新天气出错" + e.getMessage());
//
//                    }
//                });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        myHandler.sendEmptyMessage(2);

                    }
                }).start();
            }
        });
    }


    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        loadWeatherData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    ToastUtil.showShortToast("更新天气出错");
                    break;
            }
        }
    };

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                try {
//                    loadWeatherData();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

}
