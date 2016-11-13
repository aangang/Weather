package com.guofeng.weather.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.guofeng.weather.R;
import com.guofeng.weather.base.BaseActivity;
import com.guofeng.weather.util.MyUtil;
import com.guofeng.weather.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * About
 * Created by GUOFENG on 2016/11/6.
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.about_version)
    TextView about_version;

    @BindView(R.id.btn_github)
    Button btnGitHub;
    @BindView(R.id.btn_csdn)
    Button btnBlog;
    @BindView(R.id.btn_alipay)
    Button btnAlipay;
    @BindView(R.id.btn_shareApp)
    Button btnShare;
    @BindView(R.id.btn_updataApp)
    Button btnUpdata;
    @BindView(R.id.btn_bug)
    Button btnBug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarLayout.setTitle(getString(R.string.app_name));
        about_version.setText("当前版本: 2.4");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.btn_github, R.id.btn_csdn, R.id.btn_alipay, R.id.btn_shareApp, R.id.btn_updataApp, R.id.btn_bug})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_github:
                goToHtml(getString(R.string.html_guofeng_github));
                break;
            case R.id.btn_csdn:
                goToHtml(getString(R.string.html_guofeng_csdn));
                break;
            case R.id.btn_alipay:
                MyUtil.copyToClipboard(getString(R.string.guofeng_alipay), this);
                break;
            case R.id.btn_shareApp:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_txt));
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_app)));
                break;
            case R.id.btn_bug:
                ToastUtil.showShortToast("反馈意见功能暂时未添加！");
                break;
            case R.id.btn_updataApp:
                ToastUtil.showShortToast("更新软件功能暂时未添加！");
                break;
        }
    }

    //跳转网页
    private void goToHtml(String url) {
        Uri uri = Uri.parse(url); //指定网址
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW); //指定Action
        intent.setData(uri);   //设置Uri
        startActivity(intent);
    }
}
