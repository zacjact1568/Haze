package com.zack.enderweather.interactor.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.zack.enderweather.R;
import com.zack.enderweather.domain.fragment.GuideFragment;
import com.zack.enderweather.domain.view.GuideView;
import com.zack.enderweather.interactor.adapter.GuidePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuidePresenter implements Presenter<GuideView> {

    private GuideView guideView;
    private GuidePagerAdapter guidePagerAdapter;

    public GuidePresenter(GuideView guideView) {
        attachView(guideView);
    }

    @Override
    public void attachView(GuideView view) {
        this.guideView = view;
    }

    @Override
    public void detachView() {
        this.guideView = null;
    }

    public void setInitialView(FragmentManager fragmentManager) {
        guidePagerAdapter = new GuidePagerAdapter(fragmentManager, getGuidePages());
        guideView.showInitialView(guidePagerAdapter);
    }

    public void notifyViewClicked(int viewId, int currentPage) {
        switch (viewId) {
            case R.id.btn_start:
                if (currentPage != 0) {
                    guideView.onLastPageButtonClicked(currentPage - 1);
                }
                break;
            case R.id.btn_end:
                if (currentPage != guidePagerAdapter.getCount() - 1) {
                    //不是最后一页
                    guideView.onNextPageButtonClicked(currentPage + 1);
                } else {
                    //最后一页
                    guideView.onFinishButtonClicked();
                }
                break;
        }
    }

    public void notifyPageSelected(int selectedPage) {
        guideView.onPageSelected(selectedPage == 0, selectedPage == guidePagerAdapter.getCount() - 1);
    }

    private List<Fragment> getGuidePages() {
        List<Fragment> fragmentList = new ArrayList<>();
        //欢迎页
        fragmentList.add(GuideFragment.newInstance(GuideFragment.PAGE_TYPE_WELCOME));
        //位置服务页（包括权限请求）
        fragmentList.add(GuideFragment.newInstance(GuideFragment.PAGE_TYPE_LOCATION));
        //引导结束页
        fragmentList.add(GuideFragment.newInstance(GuideFragment.PAGE_TYPE_RESULT));
        return fragmentList;
    }
}
