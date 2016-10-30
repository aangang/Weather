package com.guofeng.weather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GUOFENG on 2016/10/22.
 */

public class IndexAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
//    private TabLayout mTabLayout;

    public IndexAdapter(FragmentManager fm) {
        super(fm);
    }

//    public IndexAdapter(FragmentManager fm, TabLayout tabLayout) {
//        super(fm);
//        mTabLayout = tabLayout;
//    }

    public void addTab(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
