package com.guofeng.weather.fragment;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.guofeng.weather.R;
import com.guofeng.weather.service.AutoUpdataService;
import com.guofeng.weather.util.NotificationUtil;
import com.guofeng.weather.util.SharedPreferenceUtil;

/**
 * Created by GUOFENG on 2016/11/12.
 */

public class SettingFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener,
        Preference.OnPreferenceChangeListener {

    private SharedPreferenceUtil mSharedPreferenceUtil;
    private Preference mChangeUpdate;
    private Preference mClearCache;
    private SwitchPreference mNotificationModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        mSharedPreferenceUtil = SharedPreferenceUtil.getInstance();

        mChangeUpdate = findPreference(SharedPreferenceUtil.AUTO_UPDATE);
        mClearCache = findPreference(SharedPreferenceUtil.CLEAR_CACHE);
        mNotificationModel = (SwitchPreference) findPreference(SharedPreferenceUtil.NOTIFICATION_MODEL);

        if (SharedPreferenceUtil.getInstance().getNotificationModel() != Notification.FLAG_ONGOING_EVENT) {
            mNotificationModel.setEnabled(false);
        }
        mChangeUpdate.setSummary(mSharedPreferenceUtil.getAutoUpdate() == 0 ? "关闭刷新" : "每" + mSharedPreferenceUtil.getAutoUpdate() + "个小时刷新天气信息");

        // mClearCache.setSummary(FileSizeUtil.getAutoFileOrFilesSize(BaseApplication.cacheDir + "/NetCache"));
        mChangeUpdate.setOnPreferenceClickListener(this);
        mClearCache.setOnPreferenceClickListener(this);
        mNotificationModel.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (mChangeUpdate == preference) {
            showUpdateSetDialog();
        }
        return true;
    }

    private void showUpdateSetDialog() {
        //将 SeekBar 放入 Dialog 中
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(
                R.layout.dialog_update_time,
                (ViewGroup) getActivity().findViewById(R.id.dialog_seekbar)
        );

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(dialogLayout);
        final AlertDialog alertDialog = builder.create();
        final SeekBar mSeekBar = (SeekBar) dialogLayout.findViewById(R.id.seekbar_time);
        final TextView mShowHour = (TextView) dialogLayout.findViewById(R.id.seekbar_showhour);
        TextView mOK = (TextView) dialogLayout.findViewById(R.id.seekbar_ok);
        mSeekBar.setMax(24);
        mSeekBar.setProgress(mSharedPreferenceUtil.getAutoUpdate());
        mShowHour.setText(String.format("每%s小时更新天气", mSeekBar.getProgress()));
        alertDialog.show();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mShowHour.setText(String.format("每%s小时更新天气", mSeekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPreferenceUtil.setAutoUpdate(mSeekBar.getProgress());
                mChangeUpdate.setSummary(mSharedPreferenceUtil.getAutoUpdate() == 0 ?
                        "关闭刷新" : "每" + mSharedPreferenceUtil.getAutoUpdate() + "个小时刷新天气信息");
                //需要再调用一次才能生效,不会重复的执行onCreate(),而是会调用onStart()和onStartCommand()。
                getActivity().startService(new Intent(getActivity(), AutoUpdataService.class));
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (mNotificationModel == preference) {
            SharedPreferenceUtil.getInstance().setNotificationModel(
                    (boolean) newValue ? Notification.FLAG_ONGOING_EVENT : Notification.FLAG_AUTO_CANCEL);
            if (mSharedPreferenceUtil.getNotificationModel() == Notification.FLAG_AUTO_CANCEL) {
                NotificationUtil.manager.cancelAll();
            }else{
                getActivity().startService(new Intent(getActivity(), AutoUpdataService.class));
            }
        }
        return true;
    }
}
