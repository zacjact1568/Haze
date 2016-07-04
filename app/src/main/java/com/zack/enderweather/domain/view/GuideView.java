package com.zack.enderweather.domain.view;

import com.zack.enderweather.interactor.adapter.GuidePagerAdapter;

public interface GuideView {

    void showInitialView(GuidePagerAdapter guidePagerAdapter);

    void onLastPageButtonClicked(int lastPage);

    void onNextPageButtonClicked(int nextPage);

    void onFinishButtonClicked();

    void onPageSelected(boolean isFirstPage, boolean isLastPage);

    void onPressBackKey();

    void onShowDoubleClickToast();
}
