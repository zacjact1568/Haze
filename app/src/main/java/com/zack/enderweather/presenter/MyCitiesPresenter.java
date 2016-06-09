package com.zack.enderweather.presenter;

import com.zack.enderweather.adapter.CityAdapter;
import com.zack.enderweather.database.EnderWeatherDB;
import com.zack.enderweather.event.CityAddedEvent;
import com.zack.enderweather.event.CityDeletedEvent;
import com.zack.enderweather.event.WeatherUpdatedEvent;
import com.zack.enderweather.manager.DataManager;
import com.zack.enderweather.util.LogUtil;
import com.zack.enderweather.util.Util;
import com.zack.enderweather.view.MyCitiesView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MyCitiesPresenter implements Presenter<MyCitiesView> {

    private static final String LOG_TAG = "MyCitiesPresenter";

    private MyCitiesView myCitiesView;
    private DataManager dataManager;
    private EnderWeatherDB enderWeatherDB;
    private CityAdapter cityAdapter;

    public MyCitiesPresenter(MyCitiesView myCitiesView) {
        attachView(myCitiesView);
        dataManager = DataManager.getInstance();
        enderWeatherDB = EnderWeatherDB.getInstance();
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
        cityAdapter.setOnCityItemClickListener(new CityAdapter.OnCityItemClickListener() {
            @Override
            public void onCityItemClick(int position) {
                LogUtil.d(LOG_TAG, "Click at " + position);
            }
        });
        cityAdapter.setOnUpdateButtonClickListener(new CityAdapter.OnUpdateButtonClickListener() {
            @Override
            public void onUpdateButtonClick(int position) {
                LogUtil.d(LOG_TAG, "Update at " + position);
            }
        });
        cityAdapter.setOnDeleteButtonClickListener(new CityAdapter.OnDeleteButtonClickListener() {
            @Override
            public void onDeleteButtonClick(int position) {
                myCitiesView.showCityDeletionAlertDialog(dataManager.getCityName(position), position);
            }
        });
        myCitiesView.showInitialView(cityAdapter);
    }

    public void notifyCityDeleted(int position) {
        String cityId = dataManager.getCityId(position);
        dataManager.removeFromWeatherList(position);
        cityAdapter.notifyItemRemoved(position);

        //更新天气的ViewPager
        EventBus.getDefault().post(new CityDeletedEvent());

        enderWeatherDB.deleteWeather(cityId);
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
