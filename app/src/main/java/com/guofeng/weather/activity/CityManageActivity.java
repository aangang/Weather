package com.guofeng.weather.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.guofeng.weather.R;
import com.guofeng.weather.adapter.ManageAdapter;
import com.guofeng.weather.base.ToolbarActivity;
import com.guofeng.weather.util.SharedPreferenceUtil;
import com.guofeng.weather.util.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 管理城市页
 * Created by GUOFENG on 2016/10/30.
 */
public class CityManageActivity extends ToolbarActivity {

    private ManageAdapter manageAdapter;
    private RecyclerView recyclerView;
    private Context mContext = this;
    private ArrayList<String> list = SharedPreferenceUtil.getInstance().getArray();
    @BindView(R.id.save_city)
    Button btnSaveCity;
    @BindView(R.id.add_city)
    Button btnAddCity;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_city_manage;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        intView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //选择城市回传的是RESULT_OK
            case RESULT_OK:
                String city = data.getStringExtra("CHOOSE_CITY");
                list.add(city);
                manageAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.add_city)
    public void addCity() {
        if (list.size() < 3) {
            Intent intent = new Intent(CityManageActivity.this, CityChooseActivity.class);
            startActivityForResult(intent, 100);
        } else {
            ToastUtil.showShortToast("Sorry！您最多关注三个城市哦");
        }
    }


    @OnClick(R.id.save_city)
    public void saveCity() {
        if (SharedPreferenceUtil.getInstance().saveArray(list)) {
            ToastUtil.showShortToast("保存" + list.size() + "个城市");
        }
        Intent intent = new Intent(CityManageActivity.this, MainActivity.class);
        intent.putExtra("CHANGE_CITY", 1);
        startActivity(intent);
    }

    private void intView() {
        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerview3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        manageAdapter = new ManageAdapter(mContext, list);
        recyclerView.setAdapter(manageAdapter);
        manageAdapter.setOnItemClickListener(new ManageAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                new AlertDialog.Builder(mContext)
                        .setTitle("亲爱的")
                        .setMessage("您真的不再关注 " + list.get(position) + " 的天气信息吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);
                                manageAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }
}
