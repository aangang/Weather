//package com.guofeng.weather.service;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.os.SystemClock;
//import android.support.annotation.Nullable;
//
//import com.guofeng.weather.model.WeatherInfo;
//import com.guofeng.weather.receiver.AutoUpdateReceiver;
//import com.guofeng.weather.util.ACache;
//import com.guofeng.weather.base.C;
//import com.guofeng.weather.util.ToastUtil;
//
//
//
//public class AutoUpdateService extends Service {
//
//    private ACache aCache;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        aCache = ACache.get(this);
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                updateWeather();
//            }
//        });
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        int hour = 60 * 60 * 1000;//一小时毫秒数
//        long triggerTime = SystemClock.currentThreadTimeMillis() + hour;
//
//        Intent i = new Intent(this, AutoUpdateReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent);
//
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    private void updateWeather() {
//
//        String city_code = ((WeatherInfo) aCache.getAsObject("tmpWeatherInfo")).getHeWeatherdataservice().get(0).getBasic().getId();
//        String address = "https://api.heweather.com/x3/weather?cityid="
//                + city_code + "&key="
//                + C.HEFENG_KEY;
//
//        HttpUtil.sendHttpRequest(address, new HttpCallback() {
//            @Override
//            public void onFinish(StringBuilder response) {
//                if (Utility.handleWeatherResponse(response, aCache)) {
//                    ToastUtil.showShortToast("定时更新天气完毕");
//                }
//            }
//
//            @Override
//            public void onError(Exception e) {
//                ToastUtil.showShortToast("定时更新天气失败");
//            }
//        });
//    }
//}
