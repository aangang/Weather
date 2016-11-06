package com.guofeng.weather.model.net;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 业务请求接口
 */
public interface ApiInterface {

    //基本URL
    String BASIC_URL = "https://api.heweather.com/x3/";

    //@GET注解表示get请求，@Query表示请求参数，将会以key=value方式拼接在url后面
    @GET("weather")
    Observable<WeatherAPI> mWeatherAPI(@Query("city") String city, @Query("key") String key);

    //而且在Retrofit 2.0中我们还可以在@Url里面定义完整的URL：这种情况下Base URL会被忽略。
    // @GET("http://api.fir.im/apps/latest/5630e5f1f2fc425c52000006")
    //Observable<VersionAPI> mVersionAPI(@Query("api_token") String api_token);
}
