package com.guofeng.weather.base;

import android.app.Application;
import android.content.Context;


public class BaseApplication extends Application {

    public static String cacheDir;
    private static Context myAppContext = null;

    @Override
    public void onCreate() {
        super.onCreate();

        myAppContext = getApplicationContext();
        //如果存在SD卡则将缓存写入SD卡,否则写入手机内存
        if (getApplicationContext().getExternalCacheDir() != null && ExistSDCard()) {
            cacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            cacheDir = getApplicationContext().getCacheDir().toString();
        }
    }

    private boolean ExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static Context getMyAppContext() {
        return myAppContext;
    }


    public static int getMemoryCacheSize() {
        //为了全局调用，在Application类中定义了cache大小的方法,官方建议取当前app可用内存的1/8。
        final int memClass = (int) (Runtime.getRuntime().maxMemory());
        return memClass / 4;
    }
}
