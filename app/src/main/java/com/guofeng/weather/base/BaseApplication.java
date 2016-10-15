package com.guofeng.weather.base;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class BaseApplication extends Application {

    public static Context myAppContext = null;
    public static RequestQueue queues = null;

    @Override
    public void onCreate() {
        super.onCreate();
        myAppContext = getApplicationContext();
        queues = Volley.newRequestQueue(getApplicationContext());
    }

    public static Context getMyAppContext() {
        return myAppContext;
    }

    public synchronized static RequestQueue getHttpQueues() {
        return queues;
    }

    public static int getMemoryCacheSize() {
        //为了全局调用，在Application类中定义了cache大小的方法,官方建议取当前app可用内存的1/8。
        final int memClass = (int) (Runtime.getRuntime().maxMemory());
        return memClass / 4;
    }
}
