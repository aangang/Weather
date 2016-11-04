package com.guofeng.weather.base;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.WindowManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by GUOFENG on 2016/10/20.
 */

public class BaseActivity extends RxAppCompatActivity {

    private static String TAG = BaseActivity.class.getSimpleName();

    /**
     * 设置状态栏颜色，也就是沉浸式状态栏。
     */
    @Deprecated
    public void setStatusBarColor(int color) {
        /**
         * Android4.4以上  但是抽屉有点冲突，目前就重写一个方法暂时解决4.4的问题
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //设置窗体背景透明
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);//开启状态栏
            tintManager.setStatusBarTintResource(color);//导航栏染色
            //在xml文件顶层布局加
            //android：fitsSystemWindows="true" 调整view的paingding属性来给system windows留出空间
            //android:clipToPadding="true" 控件的绘制区是否在padding里面，默认为true，false表示可在控件外面显示
        }
    }

    public void setStatusBarColorForKitkat(int color) {
        /**
         * Android4.4
         */
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void setDayTheme(AppCompatActivity activity) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // 调用 recreate() 使设置生效
        activity.recreate();
    }

    public static void setNightTheme(AppCompatActivity activity) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        // 调用 recreate() 使设置生效
        activity.recreate();
    }

    public void setTheme(boolean isNights, AppCompatActivity activity) {
        if (isNights) {
            setNightTheme(activity);
        } else {
            setDayTheme(activity);
        }
    }

    public void setTheme(AppCompatActivity activity) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        activity.recreate();
    }
}

