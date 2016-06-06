package com.zack.enderweather.presenter;

import android.support.v4.app.FragmentManager;

import com.zack.enderweather.adapter.WeatherPagerAdapter;
import com.zack.enderweather.event.CityAddedEvent;
import com.zack.enderweather.manager.DataManager;
import com.zack.enderweather.view.HomeView;

import org.greenrobot.eventbus.EventBus;

public class HomePresenter implements Presenter<HomeView> {

    private HomeView homeView;
    private DataManager dataManager;
    private WeatherPagerAdapter weatherPagerAdapter;

    public HomePresenter(HomeView homeView) {
        attachView(homeView);
        dataManager = DataManager.getInstance();
    }

    @Override
    public void attachView(HomeView view) {
        homeView = view;
    }

    @Override
    public void detachView() {
        homeView = null;
    }

    public void setInitialView(FragmentManager fragmentManager) {
        dataManager.loadWeatherFromDatabase();
        weatherPagerAdapter = new WeatherPagerAdapter(fragmentManager, dataManager.getWeatherList());
        homeView.showInitialView(weatherPagerAdapter);
    }

    public void notifyCityAdded() {
        //通知该Activity的各个Fragment更新状态
        EventBus.getDefault().post(new CityAddedEvent());
        weatherPagerAdapter.notifyDataSetChanged();
    }
}
