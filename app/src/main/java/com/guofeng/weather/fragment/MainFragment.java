package com.guofeng.weather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guofeng.weather.R;
import com.guofeng.weather.adapter.WeatherAdapter;
import com.guofeng.weather.base.BaseFragment;
import com.guofeng.weather.model.ChangeCityEvent;
import com.guofeng.weather.model.Weather;
import com.guofeng.weather.util.AMapLocationUtil;
import com.guofeng.weather.util.RetrofitSingleton;
import com.guofeng.weather.util.RxBus;
import com.guofeng.weather.util.SharedPreferenceUtil;
import com.guofeng.weather.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 主页面fragment
 * Created by GUOFENG on 2016/10/22.
 */
public class MainFragment extends BaseFragment {

    @BindView(R.id.mySwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.myRecyclerView)
    RecyclerView recyclerView;

    private static Weather mWeather = new Weather();
    private WeatherAdapter mWeatherAdapter;
    private Unbinder unbinder;
    private View view;
    String TAG = "MainFragment";

    /**
     * 懒加载数据操作,在视图创建之前初始化
     */
    @Override
    protected void lazyLoad() {
        Log.e(TAG, "lazyLoad");
        AMapLocationUtil.getDefault().startAMap();
        Load();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        RxBus.getDefault().toObserverable(ChangeCityEvent.class)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ChangeCityEvent>() {
                    @Override
                    public void onCompleted() {
                        Log.e("MainFragment onCreate", "onCompleted!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("MainFragment onCreate", e.toString());
                    }

                    @Override
                    public void onNext(ChangeCityEvent changeCityEvent) {
                        Log.e("MainFragment onCreate", "onNext!");
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.setRefreshing(true);
                            Load();
                        }
                    }
                });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main, container, false);
            unbinder = ButterKnife.bind(this, view);
        }
        isCreateVew = true;
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated");
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeResources(
                    android.R.color.holo_purple,
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light

            );
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Load();
                        }
                    }, 1000);
                }
            });
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mWeatherAdapter = new WeatherAdapter(mWeather);
        recyclerView.setAdapter(mWeatherAdapter);

    }

    private void Load() {
        fetchDataByNetWork().doOnRequest(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                swipeRefreshLayout.setRefreshing(true);
            }
        }).doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                recyclerView.setVisibility(View.GONE);
                SharedPreferenceUtil.getInstance().setCityName("北京");
            }
        }).doOnNext(new Action1<Weather>() {
            @Override
            public void call(Weather weather) {
                recyclerView.setVisibility(View.VISIBLE);
            }
        }).doOnTerminate(new Action0() {
            @Override
            public void call() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Weather>() {
                    @Override
                    public void onCompleted() {
                        ToastUtil.showShortToast("加载完毕");
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showShortToast(e.toString());

                    }

                    @Override
                    public void onNext(Weather weather) {
                        mWeather.status = weather.status;
                        mWeather.aqi = weather.aqi;
                        mWeather.basic = weather.basic;
                        mWeather.suggestion = weather.suggestion;
                        mWeather.now = weather.now;
                        mWeather.dailyForecast = weather.dailyForecast;
                        mWeather.hourlyForecast = weather.hourlyForecast;
                        setFragmentTitle(weather.basic.city);
                        mWeatherAdapter.notifyDataSetChanged();
                        //normalStyleNotification(weather);
                    }
                });
    }

    /**
     * 从网络获取
     */
    private Observable<Weather> fetchDataByNetWork() {
        String cityName = SharedPreferenceUtil.getInstance().getCityName();
        return RetrofitSingleton.getInstance().
                fetchWeather(cityName).compose(this.<Weather>bindToLifecycle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView");
        unbinder.unbind();
    }

}
