package com.zack.enderweather.domain.view;

import com.zack.enderweather.interactor.adapter.CityAdapter;

public interface MyCitiesView {

    void showInitialView(CityAdapter cityAdapter);

    void onDetectNetworkNotAvailable();

    void showCityDeletionAlertDialog(String cityName, int position);

    void onBack();
}
