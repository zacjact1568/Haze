package me.imzack.app.cold.domain.view;

public interface GuidePageView {

    void showInitialWelcomeView();

    void showInitialLocationView(boolean isLocationServiceEnabled);

    void showInitialReadyView();

    void onRequestPermissions();

    void onPermissionGranted();

    void onPermissionDenied();
}
