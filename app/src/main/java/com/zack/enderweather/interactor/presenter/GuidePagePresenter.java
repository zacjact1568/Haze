package com.zack.enderweather.interactor.presenter;

import com.zack.enderweather.R;
import com.zack.enderweather.domain.fragment.GuidePageFragment;
import com.zack.enderweather.domain.view.GuidePageView;
import com.zack.enderweather.model.preference.PreferenceDispatcher;
import com.zack.enderweather.util.Util;

public class GuidePagePresenter implements Presenter<GuidePageView> {

    private GuidePageView mGuidePageView;
    private PreferenceDispatcher mPreferenceDispatcher;

    public GuidePagePresenter(GuidePageView guidePageView) {
        attachView(guidePageView);
        mPreferenceDispatcher = PreferenceDispatcher.getInstance();
    }

    @Override
    public void attachView(GuidePageView view) {
        mGuidePageView = view;
    }

    @Override
    public void detachView() {
        mGuidePageView = null;
    }

    public void setInitialView(int pageType) {
        switch (pageType) {
            case GuidePageFragment.PAGE_TYPE_WELCOME:
                //欢迎页
                mGuidePageView.showInitialWelcomeView();
                break;
            case GuidePageFragment.PAGE_TYPE_LOCATION:
                //定位页
                boolean isEnabled = mPreferenceDispatcher.getBooleanPref(PreferenceDispatcher.KEY_PREF_LOCATION_SERVICE);
                if (!Util.isPermissionsGranted() && isEnabled) {
                    //特殊情况，将preference中的值强制置为false
                    mPreferenceDispatcher.setPref(PreferenceDispatcher.KEY_PREF_LOCATION_SERVICE, false);
                    isEnabled = false;
                }
                mGuidePageView.showInitialLocationView(isEnabled);
                break;
            case GuidePageFragment.PAGE_TYPE_READY:
                //预备页
                mGuidePageView.showInitialReadyView();
                break;
            default:
                break;
        }
    }

    public void notifyPermissionsPreviouslyGranted() {
        if (Util.isVersionBelowMarshmallow()) {
            //说明不需要动态授权
            notifyPermissionsGranted();
        } else {
            //需要动态授权，弹系统授权窗口
            mGuidePageView.onRequestPermissions();
        }
    }

    public void notifyPermissionsGranted() {
        mGuidePageView.onPermissionGranted();
        mPreferenceDispatcher.setPref(PreferenceDispatcher.KEY_PREF_LOCATION_SERVICE, true);
        //TODO 开始获取位置
    }

    public void notifyPermissionsDenied() {
        mGuidePageView.onPermissionDenied();
    }
}
