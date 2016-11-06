package com.guofeng.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 闪屏页：展现标识、数据加载、检查更新
 */
public class SplashActivity extends Activity {
    //获得主线程的Looper实例，activity
    private SwitchHandler mHandler = new SwitchHandler(Looper.getMainLooper(), this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使用handler延时发送空消息，这样Handler在延迟时间后收到消息再去执行逻辑。
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    //通过WeakReference实现对Activity的弱化引用，防止内存泄漏
    class SwitchHandler extends Handler {

        private WeakReference<SplashActivity> mWeakReference;
        SwitchHandler(Looper mLooper, SplashActivity activity) {
            super(mLooper);
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            SplashActivity splashActivity = mWeakReference.get();
            if (splashActivity != null) {
                Intent intent = new Intent(splashActivity, MainActivity.class);
                splashActivity.startActivity(intent);
                //activity切换的淡入淡出效果
                splashActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                splashActivity.finish();
            }
        }
    }
}