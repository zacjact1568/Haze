package com.zack.enderweather.domain.view;

import com.zack.enderweather.interactor.adapter.CitySearchResultAdapter;

public interface AddCityView {

    void showInitialView(CitySearchResultAdapter citySearchResultAdapter);

    void onCityAdded();

    void onDetectCityExists();

    void onSearchTextEmptied();
}
