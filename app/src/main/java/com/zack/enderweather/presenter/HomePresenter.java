package com.zack.enderweather.presenter;

import android.content.res.Resources;
import android.support.v4.app.FragmentManager;

import com.zack.enderweather.R;
import com.zack.enderweather.adapter.WeatherPagerAdapter;
import com.zack.enderweather.application.EnderWeatherApp;
import com.zack.enderweather.event.CityAddedEvent;
import com.zack.enderweather.event.CityClickedEvent;
import com.zack.enderweather.event.CityDeletedEvent;
import com.zack.enderweather.event.WeatherUpdatedEvent;
import com.zack.enderweather.manager.DataManager;
import com.zack.enderweather.view.HomeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class HomePresenter implements Presenter<HomeView> {

    private HomeView homeView;
    private DataManager dataManager;
    private WeatherPagerAdapter weatherPagerAdapter;
    private String updateSucStr, updateFaiStr;

    public HomePresenter(HomeView homeView) {
        attachView(homeView);
        dataManager = DataManager.getInstance();

        Resources resources = EnderWeatherApp.getGlobalContext().getResources();
        updateSucStr = resources.getString(R.string.toast_weather_update_successfully);
        updateFaiStr = resources.getString(R.string.toast_weather_update_failed);
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

    @Subscribe
    public void onWeatherUpdated(WeatherUpdatedEvent event) {
        //这里，处理MyCitiesPresenter和WeatherPresenter的onWeatherUpdated中可能出现冲突或重复的语句
        //标记weather为未请求更新
        dataManager.setWeatherDataUpdateStatus(event.position, false);
        //显示toast，提示更新成功或更新失败
        homeView.showToast(event.isSuc ? updateSucStr : updateFaiStr);
    }

    @Subscribe
    public void onCityClicked(CityClickedEvent event) {
        homeView.onSwitchPage(event.position);
    }
}
