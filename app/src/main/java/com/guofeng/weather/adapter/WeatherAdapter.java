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
import com.guofeng.weather.util.MyUtil;
import com.guofeng.weather.util.SharedPreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 天气信息适配器
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
                        LayoutInflater.from(mContext).inflate(R.layout.item_aqi, parent, false));
            case TYPE_TWO:
                return new HoursWeatherViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.item_hour_info, parent, false));
            case TYPE_THREE:
                return new SuggestionViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.item_suggestion, parent, false));
            case TYPE_FORE:
                return new ForecastViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.item_day_info, parent, false));
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
        @BindView(R.id.weather_now)
        TextView weatherNow;
        @BindView(R.id.weather_max)
        TextView weatherMax;
        @BindView(R.id.weather_min)
        TextView weatherMin;
        @BindView(R.id.weather_pm25)
        TextView weatherPm25;
        @BindView(R.id.weather_qlty)
        TextView weatherQuality;
        @BindView(R.id.weather_loctime)
        TextView weatherLocTime;

        NowWeatherViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bindView(Weather weather) {
            try {
                String upDate = weather.basic.update.loc;
                weatherLocTime.setText(String.format("今日 %s 发布", upDate.substring(upDate.length() - 5, upDate.length())));
                weatherNow.setText(String.format("%s℃", weather.now.tmp));
                weatherMax.setText(String.format(" %s ℃ ↑", weather.dailyForecast.get(0).tmp.max));
                weatherMin.setText(String.format(" %s ℃ ↓", weather.dailyForecast.get(0).tmp.min));
                weatherPm25.setText(String.format("PM2.5: %s μg/m³", weather.aqi.city.pm25));
                weatherQuality.setText(String.format("空气质量: %s", weather.aqi.city.qlty));
                ImageLoader.load(
                        itemView.getContext(),
                        SharedPreferenceUtil.getInstance().getInt(weather.now.cond.txt, R.mipmap.type_none),
                        weatherIcon
                );
            } catch (Exception e) {
                Log.e("NowWeatherViewHolder", e.toString());
            }
        }
    }

    class HoursWeatherViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout hourInfoLinear;
        private int num = weather.hourlyForecast.size();
        private TextView[] hourClock = new TextView[num];
        private TextView[] hourTemp = new TextView[num];
        private TextView[] hourWindSc = new TextView[num];
        private TextView[] hourWindDir = new TextView[num];
        private TextView[] hourPop = new TextView[num];

        HoursWeatherViewHolder(View view) {
            super(view);
            hourInfoLinear = (LinearLayout) itemView.findViewById(R.id.hour_info_linearlayout);
            for (int i = 0; i < num; i++) {
                View myView = View.inflate(mContext, R.layout.item_hour_info_line, null);
                hourClock[i] = (TextView) myView.findViewById(R.id.hour_clock);
                hourTemp[i] = (TextView) myView.findViewById(R.id.hour_temp);
                hourPop[i] = (TextView) myView.findViewById(R.id.hour_pop);
                hourWindSc[i] = (TextView) myView.findViewById(R.id.hour_wind_sc);
                hourWindDir[i] = (TextView) myView.findViewById(R.id.hour_wind_dir);
                hourInfoLinear.addView(myView);
            }
        }

        void bindView(Weather weather) {
            try {
                for (int i = 0; i < num; i++) {
                    String mDate = weather.hourlyForecast.get(i).date;
                    hourClock[i].setText(mDate.substring(mDate.length() - 5, mDate.length()));
                    hourTemp[i].setText(String.format("%s ℃", weather.hourlyForecast.get(i).tmp));
                    hourPop[i].setText(String.format("%s %%", weather.hourlyForecast.get(i).hum));
                    hourWindSc[i].setText(String.format("%s 级 ", weather.hourlyForecast.get(i).wind.sc));
                    hourWindDir[i].setText(String.format("%s", weather.hourlyForecast.get(i).wind.dir));
                }
            } catch (Exception e) {
                Log.e("HoursWeatherViewHolder", e.toString());
            }
        }

    }

    class SuggestionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cloth_brf)
        TextView clothBrf;
        @BindView(R.id.cloth_txt)
        TextView clothTxt;
        @BindView(R.id.sport_brf)
        TextView sportBrf;
        @BindView(R.id.sport_txt)
        TextView sportTxt;
        @BindView(R.id.travel_brf)
        TextView travelBrf;
        @BindView(R.id.travel_txt)
        TextView travelTxt;
        @BindView(R.id.flu_brf)
        TextView fluBrf;
        @BindView(R.id.flu_txt)
        TextView fluTxt;

        SuggestionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bindView(Weather weather) {
            try {

                clothBrf.setText(String.format("穿衣指数\t %s", weather.suggestion.drsg.brf));
                clothTxt.setText(weather.suggestion.drsg.txt);
                sportBrf.setText(String.format("运动指数\t %s", weather.suggestion.sport.brf));
                sportTxt.setText(weather.suggestion.sport.txt);
                travelBrf.setText(String.format("旅游指数\t %s", weather.suggestion.trav.brf));
                travelTxt.setText(weather.suggestion.trav.txt);
                fluBrf.setText(String.format("感冒指数\t %s", weather.suggestion.flu.brf));
                fluTxt.setText(weather.suggestion.flu.txt);
            } catch (Exception e) {
                Log.e("SuggestionViewHolder", e.toString());
            }
        }
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout dayInfoLinear;
        private int sum = weather.dailyForecast.size();
        private ImageView[] dayIcon = new ImageView[sum];
        private TextView[] dayDate = new TextView[sum];
        private TextView[] dayTemp = new TextView[sum];
        private TextView[] dayOther = new TextView[sum];

        ForecastViewHolder(View view) {
            super(view);
            dayInfoLinear = (LinearLayout) itemView.findViewById(R.id.day_info_linearlayout);
            for (int i = 0; i < sum; i++) {
                View myView = View.inflate(mContext, R.layout.item_day_info_line, null);
                dayIcon[i] = (ImageView) myView.findViewById(R.id.day_icon);
                dayDate[i] = (TextView) myView.findViewById(R.id.day_date);
                dayTemp[i] = (TextView) myView.findViewById(R.id.day_temp);
                dayOther[i] = (TextView) myView.findViewById(R.id.day_other);
                dayInfoLinear.addView(myView);
            }
        }

        void bindView(Weather weather) {
            try {
                //今日 明日
                dayDate[0].setText("今日");
                dayDate[1].setText("明日");
                for (int i = 0; i < weather.dailyForecast.size(); i++) {
                    if (i > 1) {
                        try {
                            dayDate[i].setText(MyUtil.dayForWeek(weather.dailyForecast.get(i).date));
                        } catch (Exception e) {
                            Log.e("ForecastViewHolder1", e.toString());
                        }
                    }
                    ImageLoader.load(
                            mContext,
                            SharedPreferenceUtil.getInstance().getInt(weather.dailyForecast.get(i).cond.txtD, R.mipmap.type_none),
                            dayIcon[i]
                    );
                    dayTemp[i].setText(String.format(
                            "%s℃ ~ %s℃",
                            weather.dailyForecast.get(i).tmp.min,
                            weather.dailyForecast.get(i).tmp.max
                    ));
                    dayOther[i].setText(String.format(
                            "%s转%s, %s级 %s, 降水率: %s%%",
                            weather.dailyForecast.get(i).cond.txtD,
                            weather.dailyForecast.get(i).cond.txtN,
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
