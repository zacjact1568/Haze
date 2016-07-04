package com.zack.enderweather.domain.view;

public interface GuidePageView {

    void showInitialWelcomeView();

    void showInitialLocationView(boolean isLocationServiceEnabled);

    void showInitialReadyView();

    void onRequestPermissions();

    void onPermissionGranted();

    void onPermissionDenied();
}
