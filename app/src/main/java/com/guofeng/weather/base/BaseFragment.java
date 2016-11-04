package com.guofeng.weather.base;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * BaseFragment
 * Created by GUOFENG on 2016/10/28
 */

public abstract class BaseFragment extends RxFragment {
    protected boolean isCreateVew = false;

    //我们在做开发的时候，一个Activity里面可能会以viewpager（或其他容器）与多个Fragment来组合使用，
    //而如果每个fragment都需要去加载数据，或从本地加载，或从网络加载，
    //那么在这个activity刚创建的时候就变成需要初始化大量资源。
    //那么，能不能做到当切换到这个fragment的时候，它才去初始化呢？
    //答案就在Fragment里的setUserVisibleHint这个方法里
    //该方法用于告诉系统，这个Fragment的UI是否是可见的。
    //所以我们只需要继承Fragment并重写该方法，
    //即可实现在fragment可见时才进行数据加载操作，即Fragment的懒加载。

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreateVew) {
            lazyLoad();
        }
    }

    //懒加载数据操作,在视图创建之前初始化
    protected abstract void lazyLoad();

    //当activity.oncreated调用后执行，首个fragment会调用
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            lazyLoad();
        }
    }

    protected void setFragmentTitle(String title) {
        ActionBar appBarLayout = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (appBarLayout != null) {
            appBarLayout.setTitle("滴答天气\t" + title);
        }
    }

}
