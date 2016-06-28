package com.zack.enderweather.interactor.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zack.enderweather.model.bean.Weather;
import com.zack.enderweather.domain.fragment.WeatherFragment;

import java.util.List;

public class WeatherPagerAdapter extends FragmentStatePagerAdapter {

    private List<Weather> weatherList;

    public WeatherPagerAdapter(FragmentManager fm, List<Weather> weatherList) {
        super(fm);
        this.weatherList = weatherList;
    }

    @Override
    public Fragment getItem(int position) {
        return WeatherFragment.newInstance(weatherList.get(position));
    }

    @Override
    public int getCount() {
        return weatherList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return ((WeatherFragment) object).isDeleted() ? POSITION_NONE : super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return weatherList.get(position).getBasicInfo().getCityName();
    }
}
