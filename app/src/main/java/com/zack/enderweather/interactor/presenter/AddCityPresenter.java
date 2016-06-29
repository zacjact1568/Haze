package com.zack.enderweather.interactor.presenter;

import android.text.TextUtils;

import com.zack.enderweather.interactor.adapter.CitySearchResultAdapter;
import com.zack.enderweather.model.bean.City;
import com.zack.enderweather.model.database.DatabaseDispatcher;
import com.zack.enderweather.model.ram.DataManager;
import com.zack.enderweather.domain.view.AddCityView;

import java.util.ArrayList;
import java.util.List;

public class AddCityPresenter implements Presenter<AddCityView> {

    private AddCityView addCityView;
    private DataManager dataManager;
    private DatabaseDispatcher databaseDispatcher;
    private CitySearchResultAdapter citySearchResultAdapter;
    private List<City> cityList;

    public AddCityPresenter(AddCityView addCityView) {
        attachView(addCityView);
        dataManager = DataManager.getInstance();
        databaseDispatcher = DatabaseDispatcher.getInstance();
        cityList = new ArrayList<>();
        citySearchResultAdapter = new CitySearchResultAdapter(cityList);
    }

    @Override
    public void attachView(AddCityView view) {
        addCityView = view;
    }

    @Override
    public void detachView() {
        addCityView = null;
    }

    public void setInitialView() {
        addCityView.showInitialView(citySearchResultAdapter);
    }

    public void notifySearchTextChanged(String input) {
        //先清除cityList中的内容
        cityList.clear();
        if (!TextUtils.isEmpty(input)) {
            //若查询关键词不为空才执行查询
            databaseDispatcher.queryCityLike(input, cityList);
            //刷新适配器
            citySearchResultAdapter.notifyDataSetChanged();
        } else {
            //需要先刷新适配器（清空列表上显示的内容）
            citySearchResultAdapter.notifyDataSetChanged();
            addCityView.onSearchTextEmptied();
        }
    }

    public void notifyCityListItemClicked(int position) {
        if (dataManager.isCityExists(cityList.get(position).getCityId())) {
            addCityView.onDetectCityExists();
        } else {
            //添加到weatherList
            dataManager.addToWeatherList(cityList.get(position).getCityId(), cityList.get(position).getCityName());
            //存储数据到数据库（此时为空数据）
            databaseDispatcher.saveWeather(dataManager.getRecentlyAddedWeather());
            addCityView.onCityAdded();
        }
    }
}
