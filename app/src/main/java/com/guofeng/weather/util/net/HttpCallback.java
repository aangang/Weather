package com.guofeng.weather.util.net;

public interface HttpCallback {

    void onFinish(StringBuilder response);

    void onError(Exception e);


}
