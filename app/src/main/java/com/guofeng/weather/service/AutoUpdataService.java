package com.guofeng.weather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.guofeng.weather.model.Weather;
import com.guofeng.weather.model.net.RetrofitSingleton;
import com.guofeng.weather.receiver.AutoUpdataReceiver;
import com.guofeng.weather.util.NotificationUtil;
import com.guofeng.weather.util.SharedPreferenceUtil;

import rx.Subscriber;


public class AutoUpdataService extends Service {
    private SharedPreferenceUtil mSharedPreferenceUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferenceUtil = SharedPreferenceUtil.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synchronized (this) {
            int settingHour = mSharedPreferenceUtil.getAutoUpdate();//用户设定小时
            new Thread(new Runnable() {
                @Override
                public void run() {
                    updateWeatherFromNet();
                }
            }).start();
            AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            int anHour = 60 * 60 * 1000; // 这是一小时的毫秒数
            long triggerAtTime = SystemClock.elapsedRealtime() + anHour * settingHour;
            Intent intent_for_receiver = new Intent(this, AutoUpdataReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent_for_receiver, 0);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }//end synchronized
        //return START_REDELIVER_INTENT;
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent localIntent = new Intent();
        localIntent.setClass(this, AutoUpdataService.class); // 销毁时重新启动Service
        this.startService(localIntent);
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    private void updateWeatherFromNet() {
        RetrofitSingleton.getInstance().fetchWeather(mSharedPreferenceUtil.getCityName())
                .subscribe(new Subscriber<Weather>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Weather weather) {
                        NotificationUtil.getInstance().sendNotification(weather);
                    }
                });
    }
}
