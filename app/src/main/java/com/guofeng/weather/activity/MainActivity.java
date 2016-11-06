package com.guofeng.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.guofeng.weather.R;
import com.guofeng.weather.adapter.IndexAdapter;
import com.guofeng.weather.base.BaseActivity;
import com.guofeng.weather.fragment.MainFragment;
import com.guofeng.weather.fragment.OtherFragment;
import com.guofeng.weather.service.AutoUpdataService;
import com.guofeng.weather.util.SharedPreferenceUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 主页
 * Created by GUOFENG on 2016/10/1.
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_nav)
    NavigationView navView;
    @BindView(R.id.layout_drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    private Unbinder unbinder;
    private IndexAdapter mIndexAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCity();
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        initView();//初始化fragment
        initDrawer();//初始化抽屉
        startAutoUpdataService();//自动更新

    }

    //singleTask模式更新数据
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//设置这个传来的最新的intent
        getCity();
    }

    //接受城市管理的保存信号
    public void getCity() {
        Intent intent = getIntent();
        int info = intent.getIntExtra("CHANGE_CITY", 0);
        if (info == 1) {
            ArrayList<String> list = SharedPreferenceUtil.getInstance().getArray();

            mIndexAdapter = new IndexAdapter(getSupportFragmentManager());
            MainFragment m = new MainFragment();
            mIndexAdapter.addTab(m, "所在地");
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    OtherFragment my = new OtherFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("FRAGMENT_CITY", list.get(i));
                    my.setArguments(bundle);
                    mIndexAdapter.addTab(my, list.get(i));
                }
            }
            viewPager.setAdapter(mIndexAdapter);
            tabLayout.setupWithViewPager(viewPager, false);
        }
    }

    //启动自动更新服务
    private void startAutoUpdataService() {
        Intent intent = new Intent(MainActivity.this, AutoUpdataService.class);
        startService(intent);
    }


    private void initView() {
        ArrayList<String> list = SharedPreferenceUtil.getInstance().getArray();
        mIndexAdapter = new IndexAdapter(getSupportFragmentManager());
        MainFragment m = new MainFragment();
        mIndexAdapter.addTab(m, "所在地");
        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                OtherFragment my = new OtherFragment();
                Bundle bundle = new Bundle();
                bundle.putString("FRAGMENT_CITY", list.get(i));
                my.setArguments(bundle);
                mIndexAdapter.addTab(my, list.get(i));
            }
        }

        viewPager.setAdapter(mIndexAdapter);
        tabLayout.setupWithViewPager(viewPager, false);

    }


    /**
     * 初始化抽屉
     */
    private void initDrawer() {
        setSupportActionBar(toolbar);//设定 Toolbar 取代原本的 actionbar
        if (navView != null) {
            navView.setNavigationItemSelectedListener(this);//设置监听
            navView.setItemIconTintList(null);//保持图标本色

            //比如布局完成滑出、布局隐藏或者布局正在滑动的时候都会有一个回调的监听事件，
            //而ActionBarDrawerToggle就是DrawerLayout事件的监听器
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this,
                    drawerLayout,
                    toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close
            );
            //该方法会自动和actionBar关联, 将开关的图片显示在了actionBar上
            toggle.syncState();
            drawerLayout.addDrawerListener(toggle);

        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.manage_cities:
                startActivity(new Intent(MainActivity.this, CityManageActivity.class));
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


}
