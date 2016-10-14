package com.guofeng.weather.base;

import android.app.Application;
import android.content.Context;


public class BaseApplication extends Application {

    public static Context myAppContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        myAppContext = getApplicationContext();
    }

    public static Context getMyAppContext() {
        return myAppContext;
    }
}
