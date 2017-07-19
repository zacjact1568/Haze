package me.imzack.app.cold.domain.view;

import me.imzack.app.cold.interactor.adapter.CityAdapter;

public interface MyCitiesView {

    void showInitialView(CityAdapter cityAdapter);

    void onDetectNetworkNotAvailable();

    void showCityDeletionAlertDialog(String cityName, int position);

    void onBack();
}
