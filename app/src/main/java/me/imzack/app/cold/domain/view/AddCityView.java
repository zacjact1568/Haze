package me.imzack.app.cold.domain.view;

import me.imzack.app.cold.interactor.adapter.CitySearchResultAdapter;

public interface AddCityView {

    void showInitialView(CitySearchResultAdapter citySearchResultAdapter);

    void onCityAdded();

    void onDetectCityExists();

    void onSearchTextEmptied();
}
