package com.zack.enderweather.domain.view;

import com.zack.enderweather.interactor.adapter.WeatherPagerAdapter;

public interface HomeView {

    void showInitialView(WeatherPagerAdapter weatherPagerAdapter);

    void showToast(String message);

    void onDetectNetworkNotAvailable();

    void onSwitchPage(int position);

    void showGuide();

    void onAddCity();
}
