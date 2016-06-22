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
import com.zack.enderweather.location.LocationHelper;
import com.zack.enderweather.manager.DataManager;
import com.zack.enderweather.preference.PreferenceHelper;
import com.zack.enderweather.util.LogUtil;
import com.zack.enderweather.util.Util;
import com.zack.enderweather.view.HomeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class HomePresenter implements Presenter<HomeView> {

    private HomeView homeView;
    private DataManager dataManager;
    private PreferenceHelper preferenceHelper;
    private WeatherPagerAdapter weatherPagerAdapter;
    private LocationHelper.PermissionDelegate mPermissionDelegate;
    private String updateSucStr, updateFaiStr;

    public HomePresenter(HomeView homeView) {
        attachView(homeView);
        dataManager = DataManager.getInstance();
        preferenceHelper = PreferenceHelper.getInstance();

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

    public void setPermissionDelegate(LocationHelper.PermissionDelegate delegate) {
        mPermissionDelegate = delegate;
    }

    public void notifyStartingUpCompleted() {
        if (preferenceHelper.getBooleanPref(PreferenceHelper.KEY_PREF_NEED_GUIDE)) {
            preferenceHelper.setPref(PreferenceHelper.KEY_PREF_NEED_GUIDE, false);
            if (mPermissionDelegate != null) {
                mPermissionDelegate.showPreviouslyRequestPermissionsDialog();
            } else {
                throw new RuntimeException("No delegate for permission request");
            }
        }
    }

    public void notifyPermissionsPreviouslyGranted() {
        if (Util.isVersionBelowMarshmallow()) {
            //说明不需要动态授权
            preferenceHelper.setPref(PreferenceHelper.KEY_PREF_LOCATION_SERVICE, true);
        } else {
            //需要动态授权，弹系统授权窗口
            mPermissionDelegate.onRequestPermissions();
        }
    }

    public void notifyPermissionsDenied() {
        mPermissionDelegate.showAddCityRequestDialog();
    }

    public void getLocationData() {
        //TODO ...
    }

    public void notifyCityAdded() {
        //通知该Activity的各个Fragment更新状态
        EventBus.getDefault().post(new CityAddedEvent());
        weatherPagerAdapter.notifyDataSetChanged();
    }

    public void notifyPermissionsGranted() {
        preferenceHelper.setPref(PreferenceHelper.KEY_PREF_LOCATION_SERVICE, true);
        //TODO 开始获取位置
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
