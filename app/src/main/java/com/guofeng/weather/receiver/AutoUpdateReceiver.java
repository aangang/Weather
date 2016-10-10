package com.guofeng.weather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.guofeng.weather.service.AutoUpdateService;


public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent_for_service = new Intent(context, AutoUpdateService.class);
        context.startService(intent_for_service);
    }
}
