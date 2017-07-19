package me.imzack.app.cold.interactor.presenter;

import android.content.res.Resources;
import android.support.v4.app.FragmentManager;

import me.imzack.app.cold.R;
import me.imzack.app.cold.application.EnderWeatherApp;
import me.imzack.app.cold.domain.view.HomeView;
import me.imzack.app.cold.event.CityClickedEvent;
import me.imzack.app.cold.event.WeatherUpdateStatusChangedEvent;
import me.imzack.app.cold.interactor.adapter.WeatherPagerAdapter;
import me.imzack.app.cold.event.CityAddedEvent;
import me.imzack.app.cold.event.CityDeletedEvent;
import me.imzack.app.cold.model.ram.DataManager;
import me.imzack.app.cold.model.preference.PreferenceDispatcher;
import me.imzack.app.cold.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class HomePresenter implements Presenter<HomeView> {

    private HomeView homeView;
    private DataManager dataManager;
    private PreferenceDispatcher preferenceDispatcher;
    private WeatherPagerAdapter weatherPagerAdapter;
    private String updateSucStr, updateFaiStr;

    public HomePresenter(HomeView homeView) {
        attachView(homeView);
        dataManager = DataManager.getInstance();
        preferenceDispatcher = PreferenceDispatcher.getInstance();

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

    public void notifyStartingUpCompleted() {
        if (preferenceDispatcher.getBooleanPref(PreferenceDispatcher.KEY_PREF_NEED_GUIDE)) {
            homeView.showGuide();
        }
    }

    public void notifyCityAdded() {
        //刷新pager，先将无天气数据的城市放到界面上
        weatherPagerAdapter.notifyDataSetChanged();
        if (Util.isNetworkAvailable()) {
            //如果网络可用，发起网络访问
            dataManager.getWeatherDataFromInternet(dataManager.getRecentlyAddedCityId());
        } else {
            //如果网络不可用，用SnackBar提示
            homeView.onDetectNetworkNotAvailable();
        }
        //通知MyCitiesFragment刷新列表（如果有的话）
        EventBus.getDefault().post(new CityAddedEvent());
    }

    public void notifyViewClicked(int viewId) {
        if (viewId == R.id.fab || viewId == R.id.btn_add_city) {
            homeView.onAddCity();
        }
    }

    @Subscribe
    public void onCityDeleted(CityDeletedEvent event) {
        weatherPagerAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onWeatherUpdateStatusChanged(WeatherUpdateStatusChangedEvent event) {
        //这里，处理MyCitiesPresenter和WeatherPresenter的onWeatherUpdated中可能出现冲突或重复的语句
        switch (event.status) {
            case WeatherUpdateStatusChangedEvent.STATUS_ON_UPDATING:
                //TODO 可以在这里处理开始更新的事件
                break;
            case WeatherUpdateStatusChangedEvent.STATUS_UPDATED_SUCCESSFUL:
                //显示toast，提示更新成功或更新失败
                homeView.showToast(updateSucStr);
                break;
            case WeatherUpdateStatusChangedEvent.STATUS_UPDATED_FAILED:
                homeView.showToast(updateFaiStr);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onCityClicked(CityClickedEvent event) {
        homeView.onSwitchPage(event.position);
    }
}
