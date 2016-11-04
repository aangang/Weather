package com.guofeng.weather.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guofeng.weather.R;

import java.util.ArrayList;

/**
 * 管理城市
 * Created by GUOFENG on 2016/11/3.
 */

public class ManageAdapter extends RecyclerView.Adapter<ManageAdapter.ManageViewHolder> {
    private Context mContext;
    private ArrayList<String> dataList;

    public ManageAdapter(Context mContext, ArrayList<String> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public ManageAdapter.ManageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ManageAdapter.ManageViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_manage, parent, false));
    }

    @Override
    public void onBindViewHolder(ManageAdapter.ManageViewHolder holder, final int position) {
        holder.bindView(dataList.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    class ManageViewHolder extends RecyclerView.ViewHolder {
        TextView manageCity;
        CardView cardView;

        ManageViewHolder(View itemView) {
            super(itemView);
            manageCity = (TextView) itemView.findViewById(R.id.item_manage_city);
            cardView = (CardView) itemView.findViewById(R.id.item_manage_city_cardview);
        }

        void bindView(String cityName) {
            manageCity.setText(cityName);
        }
    }
}
