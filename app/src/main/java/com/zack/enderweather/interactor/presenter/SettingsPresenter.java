package com.zack.enderweather.interactor.presenter;

import com.zack.enderweather.location.LocationHelper;
import com.zack.enderweather.domain.view.SettingsView;

public class SettingsPresenter implements Presenter<SettingsView> {

    private SettingsView settingsView;

    public SettingsPresenter(SettingsView settingsView) {
        attachView(settingsView);
    }

    @Override
    public void attachView(SettingsView view) {
        settingsView = view;
    }

    @Override
    public void detachView() {
        settingsView = null;
    }

    public void setInitialView() {
        settingsView.showInitialView();
    }
}
