package com.guofeng.weather.util;

import com.guofeng.weather.BuildConfig;
import com.guofeng.weather.base.BaseApplication;
import com.guofeng.weather.base.C;
import com.guofeng.weather.model.Weather;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * okhttp
 * retrofit
 * apiservice
 */
public class RetrofitSingleton {

    private static ApiInterface apiService = null;
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;

    private RetrofitSingleton() {
        initOkHttp();
        initRetrofit();
    }

    //获取单例
    public static RetrofitSingleton getInstance() {
        return SingletonHolder.mINSTANCE;
    }

    //在访问时创建单例
    private static class SingletonHolder {
        private static final RetrofitSingleton mINSTANCE = new RetrofitSingleton();
    }

    public ApiInterface getApiService() {
        return apiService;
    }

    private static void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            //HttpLoggingInterceptor 是一个拦截器，用于输出网络请求和结果的 Log
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);//第三方的日志拦截器
        }
        //缓存拦截器：离线读取本地缓存，在线获取最新数据(读取单个请求的请求头，也可统一设置)。
        File cacheFile = new File(BaseApplication.cacheDir, "/DiDaWeacherCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!MyUtil.isNetworkConnected(BaseApplication.getMyAppContext())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (MyUtil.isNetworkConnected(BaseApplication.getMyAppContext())) {
                    int maxAge = 0 * 60;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
        builder.cache(cache).addInterceptor(interceptor);
        builder.connectTimeout(15, TimeUnit.SECONDS); //设置超时
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true); //错误重连
        okHttpClient = builder.build();

    }

    private static void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASIC_URL)//网络请求URL相对固定的地址，一般包括请求协议（如http）、域名或ip、端口等
                .addConverterFactory(GsonConverterFactory.create())// 使用RxJava作为回调适配器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// 使用Gson作为数据转换器
                .client(okHttpClient)//更改其中的okhttp
                .build();
        apiService = retrofit.create(ApiInterface.class);
    }

    public Observable<Weather> fetchWeather(final String city) {
        return apiService.mWeatherAPI(city, C.HEFENG_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<WeatherAPI, Observable<WeatherAPI>>() {
                    @Override
                    public Observable<WeatherAPI> call(WeatherAPI weatherAPI) {
                        String status = weatherAPI.mHeWeatherDataService30s.get(0).status;
                        //出错
                        if ("no more requests".equals(status)) {
                            return Observable.error(new RuntimeException("Sorry! 今日API免费次数用完啦，感谢支持~"));
                        } else if ("unknown city".equals(status)) {
                            return Observable.error(new RuntimeException(String.format("Sorry！暂时不支持%s天气查询", city)));
                        }
                        return Observable.just(weatherAPI);
                    }
                })
                .map(new Func1<WeatherAPI, Weather>() {
                    @Override
                    public Weather call(WeatherAPI weatherAPI) {
                        return weatherAPI.mHeWeatherDataService30s.get(0);
                    }
                });
    }


}
