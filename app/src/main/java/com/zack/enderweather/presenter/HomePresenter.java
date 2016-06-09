package com.zack.enderweather.presenter;

import android.support.v4.app.FragmentManager;

import com.zack.enderweather.adapter.WeatherPagerAdapter;
import com.zack.enderweather.event.CityAddedEvent;
import com.zack.enderweather.event.CityDeletedEvent;
import com.zack.enderweather.manager.DataManager;
import com.zack.enderweather.view.HomeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachView() {
        homeView = null;
        EventBus.getDefault().unregister(this);
    }

    public void setInitialView(FragmentManager fragmentManager) {
        //从数据库装载天气数据
        dataManager.loadFromDatabase();
        weatherPagerAdapter = new WeatherPagerAdapter(fragmentManager, dataManager.getWeatherList());
        homeView.showInitialView(weatherPagerAdapter);
    }

    public void notifyCityAdded() {
        //通知该Activity的各个Fragment更新状态
        EventBus.getDefault().post(new CityAddedEvent());
        weatherPagerAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onCityDeleted(CityDeletedEvent event) {
        weatherPagerAdapter.notifyDataSetChanged();
    }
}
