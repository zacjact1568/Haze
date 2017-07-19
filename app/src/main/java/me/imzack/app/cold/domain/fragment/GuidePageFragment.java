package me.imzack.app.cold.domain.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.imzack.app.cold.R;
import me.imzack.app.cold.util.LogUtil;
import me.imzack.app.cold.domain.view.GuidePageView;
import me.imzack.app.cold.interactor.presenter.GuidePagePresenter;
import me.imzack.app.cold.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuidePageFragment extends Fragment implements GuidePageView {

    private static final String LOG_TAG = "GuidePageFragment";

    @BindView(R.id.image_guide)
    ImageView mImage;
    @BindView(R.id.title_guide)
    TextView mTitle;
    @BindView(R.id.dscpt_guide)
    TextView mDescription;
    @BindView(R.id.btn_guide)
    Button mButton;

    private static final String ARG_PAGE_TYPE = "page_type";

    public static final int PAGE_TYPE_WELCOME = 0;
    public static final int PAGE_TYPE_LOCATION = 1;
    public static final int PAGE_TYPE_READY = 2;

    private static final int REQ_CODE_PERMISSIONS = 0;

    private int mPageType;
    private GuidePagePresenter mGuidePagePresenter;

    public GuidePageFragment() {
        // Required empty public constructor
    }

    public static GuidePageFragment newInstance(int pageType) {
        GuidePageFragment fragment = new GuidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_TYPE, pageType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPageType = getArguments().getInt(ARG_PAGE_TYPE);
        }

        mGuidePagePresenter = new GuidePagePresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mGuidePagePresenter.setInitialView(mPageType);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGuidePagePresenter.detachView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_CODE_PERMISSIONS:
                if (grantResults.length > 0) {
                    int grantedPermissionCount = 0;
                    for (int result : grantResults) {
                        if (result == PackageManager.PERMISSION_GRANTED) {
                            grantedPermissionCount++;
                        }
                    }
                    if (grantedPermissionCount == permissions.length) {
                        //说明全部权限都已授予
                        mGuidePagePresenter.notifyPermissionsGranted();
                    } else {
                        //存在权限未授予
                        mGuidePagePresenter.notifyPermissionsDenied();
                    }
                } else {
                    LogUtil.i(LOG_TAG, "授权被打断");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    public void showInitialWelcomeView() {
        mImage.setImageResource(R.drawable.pic_cloud);
        mTitle.setText(R.string.title_page_welcome);
        mDescription.setText(R.string.dscpt_page_welcome);
        mButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showInitialLocationView(boolean isLocationServiceEnabled) {
        mImage.setImageResource(R.drawable.pic_cloud);
        mTitle.setText(R.string.title_page_location);
        if (isLocationServiceEnabled) {
            //已打开位置服务（出现这种情况是因为以前向导未完成就退出了）
            mDescription.setText(R.string.dscpt_enabled_page_location);
            mButton.setVisibility(View.INVISIBLE);
        } else {
            mDescription.setText(R.string.dscpt_disabled_page_location);
            mButton.setText(R.string.btn_page_location);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGuidePagePresenter.notifyPermissionsPreviouslyGranted();
                }
            });
        }
    }

    @Override
    public void showInitialReadyView() {
        mImage.setImageResource(R.drawable.pic_cloud);
        mTitle.setText(R.string.title_page_ready);
        mDescription.setText(R.string.dscpt_page_ready);
        mButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRequestPermissions() {
        if (Util.isPermissionsGranted()) {
            //开启app前已给了所有权限（打开app之前手动开启的）
            mGuidePagePresenter.notifyPermissionsGranted();
        } else {
            //开启app前没给权限（默认情况下）
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            requestPermissions(permissions, REQ_CODE_PERMISSIONS);
        }
    }

    @Override
    public void onPermissionGranted() {
        Toast.makeText(getContext(), R.string.toast_ls_success, Toast.LENGTH_SHORT).show();

        mDescription.setText(R.string.dscpt_enabled_page_location);
        mButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPermissionDenied() {
        Toast.makeText(getContext(), R.string.toast_ls_failure, Toast.LENGTH_SHORT).show();

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.title_dialog_ls_failure)
                .setMessage(R.string.msg_dialog_ls_failure)
                .setPositiveButton(R.string.btn_pos_dialog_ls_failure, null)
                .show();
    }
}
