package com.zack.enderweather.presenter;

import com.zack.enderweather.adapter.CityAdapter;
import com.zack.enderweather.event.CityAddedEvent;
import com.zack.enderweather.event.WeatherUpdatedEvent;
import com.zack.enderweather.manager.DataManager;
import com.zack.enderweather.util.Util;
import com.zack.enderweather.view.MyCitiesView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MyCitiesPresenter implements Presenter<MyCitiesView> {

    private MyCitiesView myCitiesView;
    private DataManager dataManager;
    private CityAdapter cityAdapter;

    public MyCitiesPresenter(MyCitiesView myCitiesView) {
        attachView(myCitiesView);
        dataManager = DataManager.getInstance();
        cityAdapter = new CityAdapter(dataManager.getWeatherList());
    }

    @Override
    public void attachView(MyCitiesView view) {
        myCitiesView = view;
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachView() {
        myCitiesView = null;
        EventBus.getDefault().unregister(this);
    }

    public void setInitialView() {
        myCitiesView.showInitialView(cityAdapter);
    }

    @Subscribe
    public void onCityAdded(CityAddedEvent event) {
        cityAdapter.notifyItemInserted(dataManager.getWeatherCount() - 1);
        if (Util.isNetworkAvailable()) {
            dataManager.getWeatherDataFromInternet(dataManager.getRecentlyAddedCityId());
        } else {
            myCitiesView.onDetectNetworkNotAvailable();
        }
    }

    @Subscribe
    public void onWeatherUpdated(WeatherUpdatedEvent event) {
        cityAdapter.notifyItemChanged(dataManager.getLocationInWeatherList(event.cityId));
    }
}
