//package com.guofeng.weather.activity;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.ListView;
//
//import com.guofeng.weather.R;
//import com.guofeng.weather.base.C;
//import com.guofeng.weather.db.WeatherDB;
//import com.guofeng.weather.model.City;
//import com.guofeng.weather.util.ACache;
//import com.guofeng.weather.util.SharedPreferenceUtil;
//import com.guofeng.weather.util.ToastUtil;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by GUOFENG on 2016/10/4.
// */
//public class CityChooseActivity extends Activity {
//
//    private WeatherDB weatherDB;//数据库操作对象
//    private ProgressDialog mProgressDialog;//进度条对话框
//    private ACache aCache;
//    private EditText editText;//搜索编辑框
//
//    private ArrayAdapter<String> mAdapter;//ListView适配器
//    private ListView mListView;//城市ListView
//    private List<String> cityNames = new ArrayList<>();//用于存放与输入的内容相匹配的城市名称字符串
//
//    private City myCity_selected;//选中的城市
//    private List<City> myCities;//用于存放与输入的内容相匹配的城市名称对象
//
//    private static final int NONE_DATA = 0;//标识是否有初始化城市数据，0是-1否
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_city_choose);
//
//        init();
//        editTextListener();
//        listAdapterSettings();
//
//    }
//
//
//    private void init() {
//        weatherDB = WeatherDB.getInstance(this);//获取数据库处理对象
//        aCache = ACache.get(this);
//
//        //先检查本地是否初始化城市数据，如果没有，则从服务器同步
//        if (weatherDB.checkDataState() == NONE_DATA) {
//            queryCitiesFromServer();
//        }
//        //获取本地存储的所有的城市
//        //myCities = queryCitiesFromLocal("");
//    }
//
//    private void editTextListener() {
//
//        //搜索框，设置文本变化监听器
//        editText = (EditText) findViewById(R.id.edit_city);
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                //每次文本变化就去本地数据库查询匹配的城市
//                myCities = queryCitiesFromLocal(s.toString());
//                //通知更新
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//    }
//
//    private void listAdapterSettings() {
//        //适配器初始化
//        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cityNames);
//        mListView = (ListView) findViewById(R.id.list_view_cities);
//        mListView.setAdapter(mAdapter);
//        //ListView的Item点击事件
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                myCity_selected = myCities.get(position);//根据点击的位置获取对应的City对象
//                //根据点击的城市从服务器获取天气数据
//                SharedPreferenceUtil.getInstance().setCityId(myCity_selected.getCity_code());
//                queryWeatherFromServer();
//            }
//        });
//    }
//
//
//    //从服务器取出所有的城市信息
//    private void queryCitiesFromServer() {
//        //国内城市api
//        String address = " https://api.heweather.com/x3/citylist?search=allchina&key="
//                + C.HEFENG_KEY;
//        showProgressDialog();
//
//        //从服务器获取数据
//        HttpUtil.sendHttpRequest(address, new HttpCallback() {
//            @Override
//            public void onFinish(StringBuilder response) {
//                //处理从服务器获取的数据
//                if (Utility.handleCityResponse(weatherDB, response.toString())) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            closeProgressDialog();
//                            weatherDB.updateDataState();//写入已读状态
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onError(final Exception e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        closeProgressDialog();
//                        ToastUtil.showShortToast(e.getMessage());
//                    }
//                });
//            }
//        });
//    }
//
//    //从本地数据库取出相似的城市名称
//    private List<City> queryCitiesFromLocal(String name) {
//        List<City> cities = weatherDB.loadCitiesByName(name);
//        cityNames.clear();
//        for (City city : cities) {
//            cityNames.add(city.getCity_name());
//        }
//        return cities;
//    }
//
//    //从服务器获取天气数据
//    private void queryWeatherFromServer() {
//
//        String address = "https://api.heweather.com/x3/weather?cityid="
//                + myCity_selected.getCity_code()
//                + "&key=" + C.HEFENG_KEY;
//        showProgressDialog();
//
//        HttpUtil.sendHttpRequest(address, new HttpCallback() {
//            @Override
//            public void onFinish(final StringBuilder response) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //将从服务器获取的JSON数据进行解析
//                        if (Utility.handleWeatherResponse(response, aCache)) {
//                            closeProgressDialog();
//                            //处理完天气数据，说明已经保存到本地，我们不用再把数据封装到Intent里面返回给WeatherActivity
//                            //可以在onActivityResult里面从本地存储中获取
//                            setResult(RESULT_OK);
//                            finish();
//                        }
//                    }
//
//                });
//
//            }
//
//            @Override
//            public void onError(Exception e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        closeProgressDialog();
//                        ToastUtil.showShortToast("城市数据更新失败");
//                    }
//                });
//            }
//        });
//    }
//
//    //显示进度条
//    private void showProgressDialog() {
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            mProgressDialog.setMessage("正在更新数据...");
//            mProgressDialog.setCanceledOnTouchOutside(false);
//        }
//        mProgressDialog.show();
//    }
//
//    //关闭进度条
//    private void closeProgressDialog() {
//        if (mProgressDialog != null)
//            mProgressDialog.dismiss();
//    }
//}
