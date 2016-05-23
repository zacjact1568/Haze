package com.zack.enderweather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zack.enderweather.R;
import com.zack.enderweather.bean.Weather;

import java.util.List;
import java.util.Locale;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private List<Weather> weatherList;

    public CityAdapter(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Weather weather = weatherList.get(position);

        holder.cityText.setText(weather.getBasicInfo().getCityName());
        holder.weatherText.setText(weather.getBasicInfo().getUpdateTime() == null ? "N/A" :
                String.format(Locale.getDefault(), "%d°C | %s", weather.getCurrentInfo().getTemperature(),
                        weather.getCurrentInfo().getCondition()));
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityText, weatherText;

        public ViewHolder(View itemView) {
            super(itemView);
            cityText = (TextView) itemView.findViewById(R.id.text_city);
            weatherText = (TextView) itemView.findViewById(R.id.text_weather);
        }
    }
}
