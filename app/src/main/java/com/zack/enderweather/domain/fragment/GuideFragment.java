package com.zack.enderweather.domain.fragment;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zack.enderweather.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuideFragment extends Fragment {

    @BindView(R.id.image_guide)
    ImageView mImage;
    @BindView(R.id.title_guide)
    TextView mTitle;
    @BindView(R.id.dscpt_guide)
    TextView mDescription;
    @BindView(R.id.btn_guide)
    Button mButton;

    public static final int PAGE_TYPE_WELCOME = 0;
    public static final int PAGE_TYPE_LOCATION = 1;
    public static final int PAGE_TYPE_RESULT = 2;

    private static final String ARG_PAGE_TYPE = "page_type";

    private int mPageType;

    public GuideFragment() {
        // Required empty public constructor
    }

    public static GuideFragment newInstance(int pageType) {
        GuideFragment fragment = new GuideFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initViews();
    }

    private void initViews() {
        switch (mPageType) {
            case PAGE_TYPE_WELCOME:
                //欢迎页
                mImage.setImageResource(R.drawable.pic_cloud);
                mTitle.setText(R.string.title_page_welcome);
                mDescription.setText(R.string.dscpt_page_welcome);
                mButton.setVisibility(View.INVISIBLE);
                break;
            case PAGE_TYPE_LOCATION:
                //定位页
                mImage.setImageResource(R.drawable.pic_cloud);
                mTitle.setText(R.string.title_page_location);
                mDescription.setText(R.string.dscpt_page_location);
                mButton.setText(R.string.btn_page_location);
                break;
            case PAGE_TYPE_RESULT:
                //结果页
                mImage.setImageResource(R.drawable.pic_cloud);
                mTitle.setText(R.string.title_page_result);
                mDescription.setText(R.string.dscpt_page_result);
                mButton.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }
}
