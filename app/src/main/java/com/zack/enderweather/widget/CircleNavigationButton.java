package com.zack.enderweather.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zack.enderweather.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CircleNavigationButton extends FrameLayout {

    private Drawable mIconDrawable;
    private int mCircleColor = Color.BLACK;

    private CircleImageView mCircle;
    private ImageView mIcon;

    public CircleNavigationButton(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleNavigationButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleNavigationButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        loadAttrs(attrs, defStyle);
        initViews();
    }

    private void loadAttrs(AttributeSet attrs, int defStyle) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CircleNavigationButton, defStyle, 0);
        mIconDrawable = ta.getDrawable(R.styleable.CircleNavigationButton_icon_src);
        mCircleColor = ta.getColor(R.styleable.CircleNavigationButton_circle_color, mCircleColor);
        ta.recycle();
    }

    private void initViews() {
        inflate(getContext(), R.layout.circle_navigation_button, this);

        mCircle = (CircleImageView) findViewById(R.id.circle_cnb);
        mIcon = (ImageView) findViewById(R.id.icon_cnb);

        mCircle.setImageDrawable(new ColorDrawable(mCircleColor));
        mIcon.setImageDrawable(mIconDrawable);
    }

    public void setCircleColor(@ColorRes int resId) {
        mCircle.setImageDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), resId)));
    }

    public void setIcon(@DrawableRes int resId) {
        mIcon.setImageResource(resId);
    }
}
