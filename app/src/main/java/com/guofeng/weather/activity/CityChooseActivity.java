package com.guofeng.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.guofeng.weather.R;
import com.guofeng.weather.adapter.CityAdapter;
import com.guofeng.weather.db.WeatherDB;
import com.guofeng.weather.db.WeatherDBHelper;
import com.guofeng.weather.model.City;
import com.guofeng.weather.model.Province;
import com.guofeng.weather.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by GUOFENG on 2016/10/4.
 */
public class CityChooseActivity extends ToolbarActivity {

    // @BindView(R.id.myRecyclerview2)
    RecyclerView myRecyclerview;

    private CityAdapter myCityAdapter;
    private ArrayList<String> dataList = new ArrayList<>();
    private int Level;
    public static final int LEVEL_PROVINCE = 1;
    public static final int LEVEL_CITY = 2;
    private List<Province> provincesList = new ArrayList<>();
    private List<City> cityList;
    private Province selectedProvince;
    private City selectedCity;
    private boolean isChecked = false;
    private String TAG = "CityChooseActivity";


    @Override
    protected int provideContentViewId() {
        return R.layout.activity_city_choose;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        myRecyclerview = (RecyclerView) findViewById(R.id.myRecyclerview2);
        //defer操作符是直到有订阅者订阅时，才通过Observable的工厂方法创建Observable并执行
        //defer操作符能够保证Observable的状态是最新的
        Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                WeatherDB.getInstance().openDatabase();//获取数据库处理对象,打开数据库
                return Observable.just(1);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        init();
                        queryProvinces();
                    }
                });
    }


    private void init() {
        myRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerview.setHasFixedSize(true);
        myCityAdapter = new CityAdapter(this, dataList);
        myRecyclerview.setAdapter(myCityAdapter);

        myCityAdapter.setOnItemClickListener(new CityAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (Level == LEVEL_PROVINCE) {
                    selectedProvince = provincesList.get(position);
                    //smoothScrollToPositon可以指定RecyclerView滑动到某个item
                    myRecyclerview.smoothScrollToPosition(0);
                    queryCities();
                } else if (Level == LEVEL_CITY) {
                    String city = MyUtil.replaceCity(cityList.get(position).CityName);

                    //SharedPreferenceUtil.getInstance().setCityName(city);
                    //RxBus.getDefault().post(new ChangeCityEvent());//发送

                    Intent intent = new Intent();
                    intent.putExtra("CHOOSE_CITY", city);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }

    /**
     * 查询全国所有的省份
     */
    private void queryProvinces() {
        getToolbar().setTitle("请您选择省份");
        Observable.defer(new Func0<Observable<Province>>() {
            @Override
            public Observable<Province> call() {
                if (provincesList.isEmpty()) {
                    provincesList = WeatherDBHelper.selectProvinces(WeatherDB.getInstance().getDatabase());
                }
                Log.w(TAG, String.valueOf(provincesList.size()));
                dataList.clear();
                return Observable.from(provincesList);
            }
        })
                .map(new Func1<Province, String>() {
                    @Override
                    public String call(Province province) {
                        return province.ProvinceName;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<List<String>>bindToLifecycle())
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        Level = LEVEL_PROVINCE;
                        myCityAdapter.notifyDataSetChanged();
                    }
                })
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> objects) {
                        dataList.addAll(objects);

                    }
                });

    }


    /**
     * 查询选中省份的所属城市
     */
    private void queryCities() {
        getToolbar().setTitle("请您选择城市");
        dataList.clear();
        myCityAdapter.notifyDataSetChanged();

        Observable.defer(new Func0<Observable<City>>() {
            @Override
            public Observable<City> call() {
                cityList = WeatherDBHelper.selectCities(WeatherDB.getInstance().getDatabase(), selectedProvince.ProvinceSort);
                return Observable.from(cityList);
            }
        }).map(new Func1<City, String>() {

            @Override
            public String call(City city) {
                return city.CityName;
            }
        }).toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<List<String>>bindToLifecycle())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        Level = LEVEL_CITY;
                        myCityAdapter.notifyDataSetChanged();
                        myRecyclerview.smoothScrollToPosition(0);
                    }
                })
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> objects) {
                        dataList.addAll(objects);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (Level == LEVEL_PROVINCE) {
            finish();
        } else {
            queryProvinces();
            myRecyclerview.smoothScrollToPosition(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WeatherDB.getInstance().closeDatabase();
    }
}
