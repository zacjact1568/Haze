package com.zack.enderweather.view;

import com.zack.enderweather.adapter.CitySearchResultAdapter;

public interface AddCityView {

    void showInitialView(CitySearchResultAdapter citySearchResultAdapter);

    void onCityAdded();

    void onDetectCityExists();
}
