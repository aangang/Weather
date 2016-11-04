package com.guofeng.weather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guofeng.weather.R;
import com.guofeng.weather.model.Weather;
import com.guofeng.weather.util.ImageLoader;
import com.guofeng.weather.util.SharedPreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GUOFENG on 2016/10/29.
 */

public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Weather weather;
    private Context mContext;
    private static final int TYPE_ONE = 0;
    private static final int TYPE_TWO = 1;
    private static final int TYPE_THREE = 2;
    private static final int TYPE_FORE = 3;

    public WeatherAdapter(Weather weather) {
        this.weather = weather;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == WeatherAdapter.TYPE_ONE) {
            return WeatherAdapter.TYPE_ONE;
        }
        if (position == WeatherAdapter.TYPE_TWO) {
            return WeatherAdapter.TYPE_TWO;
        }
        if (position == WeatherAdapter.TYPE_THREE) {
            return WeatherAdapter.TYPE_THREE;
        }
        if (position == WeatherAdapter.TYPE_FORE) {
            return WeatherAdapter.TYPE_FORE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        switch (viewType) {
            case TYPE_ONE:
                return new NowWeatherViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.item_temperature, parent, false));
            case TYPE_TWO:
                return new HoursWeatherViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.item_hour_info, parent, false));
            case TYPE_THREE:
                return new SuggestionViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.item_suggestion, parent, false));
            case TYPE_FORE:
                return new ForecastViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.item_forecast, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        switch (itemType) {
            case TYPE_ONE:
                ((NowWeatherViewHolder) holder).bindView(weather);
                break;
            case TYPE_TWO:
                ((HoursWeatherViewHolder) holder).bindView(weather);
                break;
            case TYPE_THREE:
                ((SuggestionViewHolder) holder).bindView(weather);
                break;
            case TYPE_FORE:
                ((ForecastViewHolder) holder).bindView(weather);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return weather.status != null ? 4 : 0;
    }


    class NowWeatherViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.weather_icon)
        ImageView weatherIcon;
        @BindView(R.id.temp_flu)
        TextView tempFlu;
        @BindView(R.id.temp_max)
        TextView tempMax;
        @BindView(R.id.temp_min)
        TextView tempMin;
        @BindView(R.id.temp_pm)
        TextView tempPm;
        @BindView(R.id.temp_quality)
        TextView tempQuality;

        NowWeatherViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bindView(Weather weather) {
            try {
                tempFlu.setText(String.format("%s℃", weather.now.tmp));
                tempMax.setText(String.format("↑ %s ℃", weather.dailyForecast.get(0).tmp.max));
                tempMin.setText(String.format("↓ %s ℃", weather.dailyForecast.get(0).tmp.min));
                tempPm.setText(String.format("PM2.5: %s μg/m³", weather.aqi.city.pm25));
                tempQuality.setText(String.format("空气质量-%s", weather.aqi.city.qlty));
                ImageLoader.load(
                        itemView.getContext(),
                        SharedPreferenceUtil.getInstance().getInt(weather.now.cond.txt, R.mipmap.none),
                        weatherIcon
                );
            } catch (Exception e) {
                Log.e("NowWeatherViewHolder", e.toString());
            }
        }
    }

    class HoursWeatherViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout itemHourInfoLinearlayout;
        private int num = weather.hourlyForecast.size();
        private TextView[] mClock = new TextView[num];
        private TextView[] mTemp = new TextView[num];
        private TextView[] mHumidity = new TextView[num];
        private TextView[] mWind = new TextView[num];

        HoursWeatherViewHolder(View view) {
            super(view);
            itemHourInfoLinearlayout = (LinearLayout) itemView.findViewById(R.id.item_hour_info_linearlayout);
            for (int i = 0; i < num; i++) {
                View view2 = View.inflate(mContext, R.layout.item_hour_info_line, null);
                mClock[i] = (TextView) view2.findViewById(R.id.one_clock);
                mTemp[i] = (TextView) view2.findViewById(R.id.one_temp);
                mHumidity[i] = (TextView) view2.findViewById(R.id.one_humidity);
                mWind[i] = (TextView) view2.findViewById(R.id.one_wind);
                itemHourInfoLinearlayout.addView(view2);
            }
        }

        void bindView(Weather weather) {
            try {
                for (int i = 0; i < num; i++) {
                    //s.subString(s.length-3,s.length);
                    //第一个参数是开始截取的位置，第二个是结束位置。
                    String mDate = weather.hourlyForecast.get(i).date;
                    mClock[i].setText(mDate.substring(mDate.length() - 5, mDate.length()));
                    mTemp[i].setText(String.format("%s ℃", weather.hourlyForecast.get(i).tmp));
                    mHumidity[i].setText(String.format("%s%%", weather.hourlyForecast.get(i).hum));
                    mWind[i].setText(String.format("%s Km/h", weather.hourlyForecast.get(i).wind.spd));
                }
            } catch (Exception e) {
                Log.e("HoursWeatherViewHolder", e.toString());
            }
        }

    }

    class SuggestionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cloth_brief)
        TextView clothBrief;
        @BindView(R.id.cloth_txt)
        TextView clothTxt;
        @BindView(R.id.sport_brief)
        TextView sportBrief;
        @BindView(R.id.sport_txt)
        TextView sportTxt;
        @BindView(R.id.travel_brief)
        TextView travelBrief;
        @BindView(R.id.travel_txt)
        TextView travelTxt;
        @BindView(R.id.flu_brief)
        TextView fluBrief;
        @BindView(R.id.flu_txt)
        TextView fluTxt;

        SuggestionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bindView(Weather weather) {
            try {

                clothBrief.setText(String.format("穿衣指数\t%s", weather.suggestion.drsg.brf));
                clothTxt.setText(weather.suggestion.drsg.txt);

                sportBrief.setText(String.format("运动指数\t%s", weather.suggestion.sport.brf));
                sportTxt.setText(weather.suggestion.sport.txt);

                travelBrief.setText(String.format("旅游指数\t%s", weather.suggestion.trav.brf));
                travelTxt.setText(weather.suggestion.trav.txt);

                fluBrief.setText(String.format("感冒指数\t%s", weather.suggestion.flu.brf));
                fluTxt.setText(weather.suggestion.flu.txt);
            } catch (Exception e) {
                Log.e("SuggestionViewHolder", e.toString());
            }
        }
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout forecastLinear;
        private int sum = weather.dailyForecast.size();
        private TextView[] forecastDate = new TextView[sum];
        private TextView[] forecastTemp = new TextView[sum];
        private TextView[] forecastTxt = new TextView[sum];
        private ImageView[] forecastIcon = new ImageView[sum];


        ForecastViewHolder(View view) {
            super(view);
            forecastLinear = (LinearLayout) itemView.findViewById(R.id.forecast_linear);
            for (int i = 0; i < sum; i++) {
                View view2 = View.inflate(mContext, R.layout.item_forecast_line, null);
                forecastDate[i] = (TextView) view2.findViewById(R.id.forecast_date);
                forecastTemp[i] = (TextView) view2.findViewById(R.id.forecast_temp);
                forecastTxt[i] = (TextView) view2.findViewById(R.id.forecast_txt);
                forecastIcon[i] = (ImageView) view2.findViewById(R.id.forecast_icon);
                forecastLinear.addView(view2);
            }
        }

        void bindView(Weather weather) {
            try {
                //今日 明日
                forecastDate[0].setText("今日");
                forecastDate[1].setText("明日");
                for (int i = 0; i < weather.dailyForecast.size(); i++) {
                    if (i > 1) {
                        try {
                            forecastDate[i].setText(weather.dailyForecast.get(i).date);
                        } catch (Exception e) {
                            Log.e("ForecastViewHolder1", e.toString());
                        }
                    }
                    ImageLoader.load(
                            mContext,
                            SharedPreferenceUtil.getInstance().getInt(weather.dailyForecast.get(i).cond.txtD, R.mipmap.none),
                            forecastIcon[i]
                    );
                    forecastTemp[i].setText(String.format(
                            "%s℃ - %s℃",
                            weather.dailyForecast.get(i).tmp.min,
                            weather.dailyForecast.get(i).tmp.max
                    ));
                    forecastTxt[i].setText(String.format(
                            "%s\t%s %s %s km/h\t降水率 %s%%",
                            weather.dailyForecast.get(i).cond.txtD,
                            weather.dailyForecast.get(i).wind.sc,
                            weather.dailyForecast.get(i).wind.dir,
                            weather.dailyForecast.get(i).wind.spd,
                            weather.dailyForecast.get(i).pop
                    ));
                }
            } catch (Exception e) {
                Log.e("ForecastViewHolder2", e.toString());
            }
        }
    }
}
