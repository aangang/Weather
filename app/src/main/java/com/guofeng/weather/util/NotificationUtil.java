package com.guofeng.weather.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.guofeng.weather.R;
import com.guofeng.weather.activity.MainActivity;
import com.guofeng.weather.base.BaseApplication;
import com.guofeng.weather.model.Weather;

/**
 * 通知栏
 * Created by GUOFENG on 2016/11/5.
 */

public class NotificationUtil {

    public static NotificationUtil getInstance() {
        return NotificationHolder.sInstance;
    }

    private static class NotificationHolder {
        private static final NotificationUtil sInstance = new NotificationUtil();
    }

    private NotificationUtil() {


    }

    private final int Notification_ID = 1;
    private PendingIntent pendingIntent;
    private Notification.Builder myBuilder;
    private Notification myNotification;
    public static NotificationManager manager;

    public void sendNotification(Weather weather) {
        Intent intent = new Intent(BaseApplication.getMyAppContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        pendingIntent = PendingIntent.getActivity(
                BaseApplication.getMyAppContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        myBuilder = new Notification.Builder(BaseApplication.getMyAppContext());
        myBuilder.setContentIntent(pendingIntent)
                .setContentTitle(weather.basic.city)
                .setContentText(String.format("%s     %s℃", weather.now.cond.txt, weather.now.tmp))
                .setSmallIcon(SharedPreferenceUtil.getInstance().getInt(weather.now.cond.txt, R.mipmap.type_none));

        myNotification = myBuilder.build();
        myNotification.flags = SharedPreferenceUtil.getInstance().getNotificationModel();
        manager = (NotificationManager) BaseApplication.getMyAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // tag和id可以区分不同的通知
        manager.notify(Notification_ID, myNotification);
    }
}
