package me.imzack.app.cold.domain.view;

import me.imzack.app.cold.interactor.adapter.WeatherPagerAdapter;

public interface HomeView {

    void showInitialView(WeatherPagerAdapter weatherPagerAdapter);

    void showToast(String message);

    void onDetectNetworkNotAvailable();

    void onSwitchPage(int position);

    void showGuide();

    void onAddCity();
}
