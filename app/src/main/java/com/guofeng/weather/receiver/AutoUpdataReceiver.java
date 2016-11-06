package com.guofeng.weather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.guofeng.weather.service.AutoUpdataService;

/**
 * Created by GUOFENG on 2016/11/6.
 */

public class AutoUpdataReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent_for_service = new Intent(context, AutoUpdataService.class);
        context.startService(intent_for_service);
    }
}
