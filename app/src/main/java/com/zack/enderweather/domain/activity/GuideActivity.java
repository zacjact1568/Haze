package com.zack.enderweather.domain.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.zack.enderweather.R;
import com.zack.enderweather.domain.view.GuideView;
import com.zack.enderweather.interactor.adapter.GuidePagerAdapter;
import com.zack.enderweather.interactor.presenter.GuidePresenter;
import com.zack.enderweather.util.LogUtil;
import com.zack.enderweather.widget.CircleNavigationButton;
import com.zack.enderweather.widget.EnhancedViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends BaseActivity implements GuideView {

    private static final String LOG_TAG = "GuideActivity";

    @BindView(R.id.pager_guide)
    EnhancedViewPager guidePager;
    @BindView(R.id.btn_start)
    CircleNavigationButton startButton;
    @BindView(R.id.btn_end)
    CircleNavigationButton endButton;

    private GuidePresenter guidePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        guidePresenter = new GuidePresenter(this);

        guidePresenter.setInitialView(getSupportFragmentManager());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        guidePresenter.detachView();
    }

    @Override
    public void onBackPressed() {
        guidePresenter.notifyBackPressed();
    }

    @Override
    public void showInitialView(GuidePagerAdapter guidePagerAdapter) {
        guidePager.setAdapter(guidePagerAdapter);
        guidePager.setScrollingEnabled(false);
        guidePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                guidePresenter.notifyPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onLastPageButtonClicked(int lastPage) {
        guidePager.setCurrentItem(lastPage);
    }

    @Override
    public void onNextPageButtonClicked(int nextPage) {
        guidePager.setCurrentItem(nextPage);
    }

    @Override
    public void onFinishButtonClicked() {
        finish();
    }

    @Override
    public void onPageSelected(boolean isFirstPage, boolean isLastPage) {
        startButton.setVisibility(isFirstPage ? View.GONE : View.VISIBLE);
        endButton.setIcon(isLastPage ? R.drawable.ic_check_white_24dp
                : R.drawable.ic_arrow_forward_white_24dp);
    }

    @Override
    public void onPressBackKey() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void onShowDoubleClickToast() {
        Toast.makeText(this, R.string.toast_double_click_exit, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.btn_start, R.id.btn_end})
    public void onClick(View view) {
        guidePresenter.notifyViewClicked(view.getId(), guidePager.getCurrentItem());
    }
}
