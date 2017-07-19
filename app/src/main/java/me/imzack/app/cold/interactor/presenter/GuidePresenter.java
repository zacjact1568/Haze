package me.imzack.app.cold.interactor.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import me.imzack.app.cold.R;
import me.imzack.app.cold.domain.fragment.GuidePageFragment;
import me.imzack.app.cold.domain.view.GuideView;
import me.imzack.app.cold.model.preference.PreferenceDispatcher;
import me.imzack.app.cold.interactor.adapter.GuidePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuidePresenter implements Presenter<GuideView> {

    private GuideView guideView;
    private GuidePagerAdapter guidePagerAdapter;
    private PreferenceDispatcher mPreferenceDispatcher;
    private long lastBackKeyPressedTime;

    public GuidePresenter(GuideView guideView) {
        attachView(guideView);
        mPreferenceDispatcher = PreferenceDispatcher.getInstance();
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
                    mPreferenceDispatcher.setPref(PreferenceDispatcher.KEY_PREF_NEED_GUIDE, false);
                    guideView.onFinishButtonClicked();
                }
                break;
        }
    }

    public void notifyPageSelected(int selectedPage) {
        guideView.onPageSelected(selectedPage == 0, selectedPage == guidePagerAdapter.getCount() - 1);
    }

    public void notifyBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackKeyPressedTime < 1500) {
            guideView.onPressBackKey();
        } else {
            lastBackKeyPressedTime = currentTime;
            guideView.onShowDoubleClickToast();
        }
    }

    private List<Fragment> getGuidePages() {
        List<Fragment> fragmentList = new ArrayList<>();
        //欢迎页
        fragmentList.add(GuidePageFragment.newInstance(GuidePageFragment.PAGE_TYPE_WELCOME));
        //位置服务页（包括权限请求）
        fragmentList.add(GuidePageFragment.newInstance(GuidePageFragment.PAGE_TYPE_LOCATION));
        //引导结束页
        fragmentList.add(GuidePageFragment.newInstance(GuidePageFragment.PAGE_TYPE_READY));
        return fragmentList;
    }
}
