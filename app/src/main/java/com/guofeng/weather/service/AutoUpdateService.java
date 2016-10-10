package com.guofeng.weather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import com.guofeng.weather.model.WeatherBasicInfo;
import com.guofeng.weather.receiver.AutoUpdateReceiver;
import com.guofeng.weather.util.ACache;
import com.guofeng.weather.util.C;
import com.guofeng.weather.util.HttpCallback;
import com.guofeng.weather.util.HttpUtil;
import com.guofeng.weather.util.Utility;


public class AutoUpdateService extends Service {

    ACache aCache;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        });

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int hour = 60 * 60 * 1000;
        long triggerTime = SystemClock.currentThreadTimeMillis() + hour;
        Intent intent_for_receiver = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        aCache = ACache.get(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void updateWeather() {

        String city_code = ((WeatherBasicInfo) aCache.getAsObject("basicInfo")).getCityId();
        String address = "https://api.heweather.com/x3/weather?cityid="
                + city_code + "&key="
                + C.HEFENG_KEY;

        HttpUtil.sendHttpRequest(address, new HttpCallback() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(response, aCache);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
