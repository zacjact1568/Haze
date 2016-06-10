package com.zack.enderweather.view;

import com.zack.enderweather.adapter.CityAdapter;

public interface MyCitiesView {

    void showInitialView(CityAdapter cityAdapter);

    void onDetectNetworkNotAvailable();

    void showCityDeletionAlertDialog(String cityName, int position);

    void showToast(String message);
}
