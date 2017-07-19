package me.imzack.app.cold.interactor.presenter;

import me.imzack.app.cold.domain.view.SettingsView;

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
