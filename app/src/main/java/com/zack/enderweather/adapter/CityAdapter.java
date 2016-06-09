package com.zack.enderweather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zack.enderweather.R;
import com.zack.enderweather.bean.Weather;

import java.util.List;
import java.util.Locale;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private List<Weather> weatherList;

    private OnCityItemClickListener onCityItemClickListener;
    private OnUpdateButtonClickListener onUpdateButtonClickListener;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;

    public CityAdapter(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Weather weather = weatherList.get(position);

        holder.cityText.setText(weather.getBasicInfo().getCityName());
        holder.weatherText.setText(weather.getBasicInfo().getUpdateTime().isEmpty() ? "--°C | --" :
                String.format("%s°C | %s", weather.getCurrentInfo().getTemperature(),
                        weather.getCurrentInfo().getCondition()));

        if (onCityItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCityItemClickListener.onCityItemClick(holder.getLayoutPosition());
                }
            });
        }
        if (onUpdateButtonClickListener != null) {
            holder.updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onUpdateButtonClickListener.onUpdateButtonClick(holder.getLayoutPosition());
                }
            });
        }
        if (onDeleteButtonClickListener != null) {
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteButtonClickListener.onDeleteButtonClick(holder.getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityText, weatherText;
        ImageView updateButton, deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            cityText = (TextView) itemView.findViewById(R.id.text_city);
            weatherText = (TextView) itemView.findViewById(R.id.text_weather);
            updateButton = (ImageView) itemView.findViewById(R.id.btn_update);
            deleteButton = (ImageView) itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface OnCityItemClickListener {
        void onCityItemClick(int position);
    }

    public void setOnCityItemClickListener(OnCityItemClickListener listener) {
        onCityItemClickListener = listener;
    }

    public interface OnUpdateButtonClickListener {
        void onUpdateButtonClick(int position);
    }

    public void setOnUpdateButtonClickListener(OnUpdateButtonClickListener listener) {
        onUpdateButtonClickListener = listener;
    }

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(int position);
    }

    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        onDeleteButtonClickListener = listener;
    }
}
