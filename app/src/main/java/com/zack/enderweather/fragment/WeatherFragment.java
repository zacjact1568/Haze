package com.zack.enderweather.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zack.enderweather.R;
import com.zack.enderweather.bean.FormattedWeather;
import com.zack.enderweather.bean.Weather;
import com.zack.enderweather.presenter.WeatherPresenter;
import com.zack.enderweather.view.WeatherView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeatherFragment extends Fragment implements WeatherView {

    @BindView(R.id.text_city_name)
    TextView cityNameText;
    @BindView(R.id.text_temperature)
    TextView temperatureText;
    @BindView(R.id.text_condition)
    TextView conditionText;
    @BindView(R.id.text_update_time)
    TextView updateTimeText;
    @BindView(R.id.text_sensible_temp)
    TextView sensibleTempText;
    @BindView(R.id.text_temp_range)
    TextView tempRangeText;
    @BindView(R.id.text_air_quality)
    TextView airQualityText;

    private static final String ARG_WEATHER = "weather";

    private Weather weather;
    private WeatherPresenter weatherPresenter;
    private TextView[] weekTexts, dateTexts, conditionTexts, tempRangeTexts;

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

        weekTexts = new TextView[]{
                (TextView) view.findViewById(R.id.text_week0),
                (TextView) view.findViewById(R.id.text_week1),
                (TextView) view.findViewById(R.id.text_week2),
                (TextView) view.findViewById(R.id.text_week3),
                (TextView) view.findViewById(R.id.text_week4),
                (TextView) view.findViewById(R.id.text_week5),
                (TextView) view.findViewById(R.id.text_week6)
        };
        dateTexts = new TextView[]{
                (TextView) view.findViewById(R.id.text_date0),
                (TextView) view.findViewById(R.id.text_date1),
                (TextView) view.findViewById(R.id.text_date2),
                (TextView) view.findViewById(R.id.text_date3),
                (TextView) view.findViewById(R.id.text_date4),
                (TextView) view.findViewById(R.id.text_date5),
                (TextView) view.findViewById(R.id.text_date6)
        };
        conditionTexts = new TextView[]{
                (TextView) view.findViewById(R.id.text_condition0),
                (TextView) view.findViewById(R.id.text_condition1),
                (TextView) view.findViewById(R.id.text_condition2),
                (TextView) view.findViewById(R.id.text_condition3),
                (TextView) view.findViewById(R.id.text_condition4),
                (TextView) view.findViewById(R.id.text_condition5),
                (TextView) view.findViewById(R.id.text_condition6)
        };
        tempRangeTexts = new TextView[]{
                (TextView) view.findViewById(R.id.text_temp_range0),
                (TextView) view.findViewById(R.id.text_temp_range1),
                (TextView) view.findViewById(R.id.text_temp_range2),
                (TextView) view.findViewById(R.id.text_temp_range3),
                (TextView) view.findViewById(R.id.text_temp_range4),
                (TextView) view.findViewById(R.id.text_temp_range5),
                (TextView) view.findViewById(R.id.text_temp_range6)
        };

        weatherPresenter.setInitialView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        weatherPresenter.detachView();
    }

    @Override
    public void showInitialView(FormattedWeather formattedWeather) {
        setView(formattedWeather);
    }

    @Override
    public void onWeatherUpdated(FormattedWeather formattedWeather) {
        setView(formattedWeather);
    }

    private void setView(FormattedWeather fw) {
        cityNameText.setText(fw.getCityName());
        temperatureText.setText(fw.getTemperature());
        conditionText.setText(fw.getCondition());
        updateTimeText.setText(fw.getUpdateTime());

        sensibleTempText.setText(fw.getSensibleTemp());
        tempRangeText.setText(fw.getTempRange());
        airQualityText.setText(fw.getAirQuality());

        //当没有数据时，weeks等四个数组为null
        if (fw.getWeeks() != null && fw.getDates() != null
                && fw.getConditions() != null && fw.getTempRanges() != null) {
            for (int i = 0; i < Weather.DAILY_FORECAST_LENGTH; i++) {
                weekTexts[i].setText(fw.getWeeks()[i]);
                dateTexts[i].setText(fw.getDates()[i]);
                conditionTexts[i].setText(fw.getConditions()[i]);
                tempRangeTexts[i].setText(fw.getTempRanges()[i]);
            }
        }
    }

    @OnClick(R.id.layout_daily_forecast)
    public void onClick() {
        //TODO Daily forecast details
    }
}
