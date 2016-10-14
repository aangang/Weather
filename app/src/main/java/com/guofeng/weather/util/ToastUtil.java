package com.guofeng.weather.util;

import android.widget.Toast;

import com.guofeng.weather.base.BaseApplication;

/**
 * Toast 工具类
 */
public class ToastUtil {

    public static void showShortToast(String msg) {
        Toast.makeText(BaseApplication.getMyAppContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String msg) {
        Toast.makeText(BaseApplication.getMyAppContext(), msg, Toast.LENGTH_LONG).show();
    }
}
