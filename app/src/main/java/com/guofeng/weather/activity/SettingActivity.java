package com.guofeng.weather.activity;

import android.os.Bundle;

import com.guofeng.weather.R;
import com.guofeng.weather.base.ToolbarActivity;
import com.guofeng.weather.fragment.SettingFragment;

/**
 * Created by GUOFENG on 2016/11/12.
 */

public class SettingActivity extends ToolbarActivity {

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(R.id.setting_framelayout, new SettingFragment()).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getToolbar().setTitle("设置");
    }

    @Override
    public boolean canBack() {
        return true;
    }
}
