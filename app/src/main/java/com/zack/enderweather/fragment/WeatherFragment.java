package com.zack.enderweather.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zack.enderweather.R;
import com.zack.enderweather.bean.Weather;
import com.zack.enderweather.presenter.WeatherPresenter;
import com.zack.enderweather.view.WeatherView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherFragment extends Fragment implements WeatherView {

    @BindView(R.id.text_city_name)
    TextView cityNameText;
    @BindView(R.id.text_temperature)
    TextView temperatureText;
    @BindView(R.id.text_condition)
    TextView conditionText;
    @BindView(R.id.text_update_time)
    TextView updateTimeText;

    private static final String ARG_WEATHER = "weather";

    private Weather weather;
    private WeatherPresenter weatherPresenter;

    public WeatherFragment() {
        // Required empty public constructor
    }

    public static WeatherFragment newInstance(Weather weather) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_WEATHER, weather);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weather = getArguments().getParcelable(ARG_WEATHER);
        }

        weatherPresenter = new WeatherPresenter(this, weather);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        weatherPresenter.setInitialView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        weatherPresenter.detachView();
    }

    @Override
    public void showInitialView(String cityName, String updateTime, int temperature, String condition) {
        cityNameText.setText(cityName);
        updateTimeText.setText(updateTime == null ? "Not updated yet" : updateTime);
        temperatureText.setText(updateTime == null ? "N/A°" : String.valueOf(temperature));
        conditionText.setText(updateTime == null ? "N/A" : condition);
    }
}
