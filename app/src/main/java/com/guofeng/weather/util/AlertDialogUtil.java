package com.guofeng.weather.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by GUOFENG on 2016/11/3.
 */

public class AlertDialogUtil {

    public static AlertDialogUtil getInstance() {
        return AlertDialogUtil.AlertDialogHolder.sInstance;
    }

    private static class AlertDialogHolder {
        private static final AlertDialogUtil sInstance = new AlertDialogUtil();

    }

    private boolean myChoose = false;

    public boolean createAlertDialog(Context mContext, String title, String message) {

        new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myChoose = true;
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myChoose = false;
                        //dialog.dismiss();
                    }
                })
                //.create()
                .show();
        return myChoose;
    }
}
