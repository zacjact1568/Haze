package me.imzack.app.cold.domain.view;

import me.imzack.app.cold.interactor.adapter.GuidePagerAdapter;

public interface GuideView {

    void showInitialView(GuidePagerAdapter guidePagerAdapter);

    void onLastPageButtonClicked(int lastPage);

    void onNextPageButtonClicked(int nextPage);

    void onFinishButtonClicked();

    void onPageSelected(boolean isFirstPage, boolean isLastPage);

    void onPressBackKey();

    void onShowDoubleClickToast();
}