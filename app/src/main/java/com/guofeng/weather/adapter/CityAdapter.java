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
 * 城市列表适配器
 * Created by GUOFENG on 2016/11/2.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private Context mContext;
    private ArrayList<String> dataList;

    public CityAdapter(Context mContext, ArrayList<String> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public CityAdapter.CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_city, parent, false));
    }

    @Override
    public void onBindViewHolder(CityAdapter.CityViewHolder holder, final int position) {
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

    //---------------
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }


    class CityViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.item_city)
        TextView itemCity;
//        @BindView(R.id.item_city_cardview)
        CardView cardView;

        CityViewHolder(View itemView) {
            super(itemView);
            itemCity = (TextView) itemView.findViewById(R.id.item_city);
            cardView = (CardView) itemView.findViewById(R.id.item_city_cardview);
        }

        void bindView(String cityName) {
            itemCity.setText(cityName);
        }
    }

}
