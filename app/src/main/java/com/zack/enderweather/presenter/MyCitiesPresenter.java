package com.zack.enderweather.presenter;

import com.zack.enderweather.adapter.CityAdapter;
import com.zack.enderweather.database.EnderWeatherDB;
import com.zack.enderweather.event.CityAddedEvent;
import com.zack.enderweather.event.CityClickedEvent;
import com.zack.enderweather.event.CityDeletedEvent;
import com.zack.enderweather.event.WeatherUpdatedEvent;
import com.zack.enderweather.manager.DataManager;
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
                myCitiesView.onBack();
                EventBus.getDefault().post(new CityClickedEvent(position));
            }
        });
        cityAdapter.setOnUpdateButtonClickListener(new CityAdapter.OnUpdateButtonClickListener() {
            @Override
            public void onUpdateButtonClick(int position) {
                if (dataManager.getWeatherDataUpdateStatus(position)) {
                    //说明现在正在更新，不响应请求
                    return;
                }
                updateWeather(position);
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

    private void updateWeather(int position) {
        if (Util.isNetworkAvailable()) {
            //标记weather为已请求更新
            dataManager.setWeatherDataUpdateStatus(position, true);
            //刷新适配器（显示出正在更新的状态）
            cityAdapter.notifyItemChanged(position);
            //开始更新数据
            dataManager.getWeatherDataFromInternet(dataManager.getCityId(position));
        } else {
            //显示网络不可用的SnackBar
            myCitiesView.onDetectNetworkNotAvailable();
        }
    }

    @Subscribe
    public void onCityAdded(CityAddedEvent event) {
        int position = dataManager.getRecentlyAddedLocation();
        cityAdapter.notifyItemInserted(position);
        updateWeather(position);
    }

    @Subscribe
    public void onWeatherUpdated(WeatherUpdatedEvent event) {
        //刷新适配器（若成功更新则表现为刷新数据且取消正在更新的状态，若更新失败则表现为仅取消正在更新的状态）
        cityAdapter.notifyItemChanged(event.position);
    }
}
