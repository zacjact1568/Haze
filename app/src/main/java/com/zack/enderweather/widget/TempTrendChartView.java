package com.zack.enderweather.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zack.enderweather.R;
import com.zack.enderweather.util.LogUtil;

public class TempTrendChartView extends View {

    //private static final int PADDING_HORIZONTAL = 0;
    private static final int PADDING_VERTICAL = 80;

    private static final float STROKE_WIDTH_POINT = 15f;
    private static final float STROKE_WIDTH_POINT_EDGE = 25f;
    private static final float STROKE_WIDTH_LINE = 5f;

    private static final float TEXT_BASE_OFFSET = 40f;

    /** 温度最大值的数组 */
    private int[] mMaxTempArray;
    /** 温度最小值的数组 */
    private int[] mMinTempArray;

    /** 温度点坐标数组 */
    private float[] mPointCoords;
    /** 温度趋势连线坐标数组 */
    private float[] mLineCoords;

    private Paint mPaint;
    private int mTempPointColor = Color.BLACK;
    private int mTempPointEdgeColor = Color.GRAY;
    private int mTempTrendLineColor = Color.BLACK;

    private TextPaint mTextPaint;
    private float mTempTextSize = 0f;
    private int mTempTextColor = Color.BLACK;
    /*private float mTextWidth;*/
    private float mMaxTempTextOffset;
    private float mMinTempTextOffset;

    /*private String mExampleString;
    private int mExampleColor = Color.RED;
    private Drawable mExampleDrawable;*/

    public TempTrendChartView(Context context) {
        super(context);
        init(null, 0);
    }

    public TempTrendChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TempTrendChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        //加载自定义的属性
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TempTrendChartView, defStyle, 0);
        mTempPointColor = ta.getColor(R.styleable.TempTrendChartView_temp_point_color, mTempPointColor);
        mTempPointEdgeColor = ta.getColor(R.styleable.TempTrendChartView_temp_point_edge_color, mTempPointEdgeColor);
        mTempTrendLineColor = ta.getColor(R.styleable.TempTrendChartView_temp_trend_line_color, mTempTrendLineColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with values that should fall on pixel boundaries.
        mTempTextSize = ta.getDimension(R.styleable.TempTrendChartView_temp_text_size, mTempTextSize);
        mTempTextColor = ta.getColor(R.styleable.TempTrendChartView_temp_text_color, mTempTextColor);
        ta.recycle();

        mPaint = new Paint();
        //设置画笔模式为填充
        mPaint.setStyle(Paint.Style.FILL);
        //设置画笔形状
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //设置抗锯齿
        mPaint.setAntiAlias(true);

        mTextPaint = new TextPaint();
        //设置画笔模式为填充
        mTextPaint.setStyle(Paint.Style.FILL);
        //设置字体大小
        mTextPaint.setTextSize(mTempTextSize);
        //设置字体颜色
        mTextPaint.setColor(mTempTextColor);
        //设置抗锯齿
        mTextPaint.setAntiAlias(true);
        //设置对齐方式为中心对齐
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        //文本高度
        float textHeight = mTextPaint.descent() - mTextPaint.ascent();
        //高、低温文本相对点的偏移（文本是以基线为准的）
        mMaxTempTextOffset = TEXT_BASE_OFFSET - textHeight / 2 + mTextPaint.descent();
        mMinTempTextOffset = TEXT_BASE_OFFSET + textHeight / 2 - mTextPaint.descent();

        /*// Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();

        if (ta.hasValue(R.styleable.TempTrendChartView_exampleDrawable)) {
            mExampleDrawable = ta.getDrawable(R.styleable.TempTrendChartView_exampleDrawable);
            if (mExampleDrawable != null) {
                mExampleDrawable.setCallback(this);
            }
        }*/
    }

    /*private void invalidateTextPaintAndMeasurements() {

        mTextWidth = mTextPaint.measureText(mExampleString);

    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mMaxTempArray == null && mMinTempArray == null) {
            //当没有数据时，不进行绘制
            return;
        }

        int contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int contentHeight = getHeight() - getPaddingTop() - getPaddingBottom() - PADDING_VERTICAL * 2;
        int contentStartX = getPaddingLeft();
        int contentStartY = getPaddingTop() + PADDING_VERTICAL;

        //计算温度点坐标
        calculatePointCoords(contentWidth, contentHeight, contentStartX, contentStartY);
        //计算温度趋势连线坐标
        calculateLineCoords();

        //设置画笔颜色
        mPaint.setColor(mTempPointEdgeColor);
        //设置画笔宽度
        mPaint.setStrokeWidth(STROKE_WIDTH_POINT_EDGE);
        //画温度点边缘
        canvas.drawPoints(mPointCoords, mPaint);

        mPaint.setColor(mTempPointColor);
        mPaint.setStrokeWidth(STROKE_WIDTH_POINT);
        //画温度点
        canvas.drawPoints(mPointCoords, mPaint);

        mPaint.setColor(mTempTrendLineColor);
        mPaint.setStrokeWidth(STROKE_WIDTH_LINE);
        //画温度连线
        canvas.drawLines(mLineCoords, mPaint);

        //画温度值
        for (int i = 0; i < mMaxTempArray.length; i++) {
            //高温
            canvas.drawText(
                    String.valueOf(mMaxTempArray[i]),
                    mPointCoords[i * 2],
                    //paddingLeft + (contentWidth - mTextWidth) / 2,
                    mPointCoords[i * 2 + 1] - mMaxTempTextOffset,
                    //paddingTop + (contentHeight + mTextHeight) / 2,
                    mTextPaint
            );
        }
        for (int i = 0; i < mMinTempArray.length; i++) {
            //低温
            canvas.drawText(
                    String.valueOf(mMinTempArray[i]),
                    mPointCoords[mMaxTempArray.length * 2 + i * 2],
                    mPointCoords[mMaxTempArray.length * 2 + i * 2 + 1] + mMinTempTextOffset,
                    mTextPaint
            );
        }

        /*// Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }*/
    }

    /**
     * 设置温度最大值的数组和温度最小值的数组
     * @param maxTempArray 温度最大值的数组
     * @param minTempArray 温度最小值的数组
     */
    public void setTempArray(int[] maxTempArray, int[] minTempArray) {
        if (maxTempArray.length == minTempArray.length) {
            mMaxTempArray = maxTempArray;
            mMinTempArray = minTempArray;
            //初始化温度点坐标数组
            mPointCoords = new float[(mMaxTempArray.length + mMinTempArray.length) * 2];
            //初始化温度趋势连线坐标数组
            mLineCoords = new float[(mMaxTempArray.length + mMinTempArray.length) * 4 - 8];
            //重新绘制
            invalidate();
        } else {
            throw new RuntimeException("Lengths of max and min temperature array are not equal");
        }
    }

    /**
     * 计算温度点的坐标
     * @param width 可绘制区域的宽度
     * @param height 可绘制区域的高度
     * @param startX 起始x坐标
     * @param startY 起始y坐标
     */
    private void calculatePointCoords(int width, int height, int startX, int startY) {

        //最高温度
        int maxTemp = mMaxTempArray[0];
        for (int temp : mMaxTempArray) {
            if (temp > maxTemp) {
                maxTemp = temp;
            }
        }
        //最低温度
        int minTemp = mMinTempArray[0];
        for (int temp : mMinTempArray) {
            if (temp < minTemp) {
                minTemp = temp;
            }
        }

        //列、行数量
        int columnCount = mMaxTempArray.length;
        int rowCount = maxTemp - minTemp + 1;

        //每个区间的宽高
        float sectionWidth = width / columnCount;
        float sectionHeight = height / rowCount;

        //计算所有坐标
        for (int i = 0; i < mPointCoords.length; i++) {
            if (i % 2 == 0) {
                //偶数：x坐标
                if (i < mMaxTempArray.length * 2) {
                    //高温部分
                    mPointCoords[i] = startX + sectionWidth / 2 + sectionWidth * (i / 2);
                } else {
                    //低温部分，数值和高温部分相同
                    mPointCoords[i] = mPointCoords[i - mMaxTempArray.length * 2];
                }
            } else {
                //奇数：y坐标（在屏幕上由上至下遍历）
                for (int j = 0; j < rowCount; j++) {
                    if (i < mMaxTempArray.length * 2) {
                        //高温部分
                        if (maxTemp - j == mMaxTempArray[(i - 1) / 2]) {
                            //说明通过mMaxTempArray取出的温度在第j个区间
                            mPointCoords[i] = startY + sectionHeight / 2 + sectionHeight * j;
                            break;
                        }
                    } else {
                        //低温部分
                        if (maxTemp - j == mMinTempArray[(i - mMaxTempArray.length * 2 - 1) / 2]) {
                            //说明通过mMinTempArray取出的温度在第j个区间
                            mPointCoords[i] = startY + sectionHeight / 2 + sectionHeight * j;
                            break;
                        }
                    }
                }
            }
        }
    }

    /** 计算温度趋势连线的坐标 */
    private void calculateLineCoords() {
        for (int i = 0; i < mLineCoords.length; i++) {
            if (i == 0 || i == 1) {
                //第一个（高温区的第一个）
                mLineCoords[i] = mPointCoords[i];
            } else if (i >= 2 && i <= mLineCoords.length / 2 - 3) {
                //高温区，需要重复取点
                //每4个点为一组
                if ((i - 2) % 4 == 0) {
                    //第1个点
                    mLineCoords[i] = mPointCoords[(i - 2) / 2 + 2];
                } else if ((i - 2) % 4 == 1) {
                    //第2个点
                    mLineCoords[i] = mPointCoords[(i - 3) / 2 + 3];
                } else if ((i - 2) % 4 == 2) {
                    //第3个点
                    mLineCoords[i] = mPointCoords[(i - 4) / 2 + 2];
                } else if ((i - 2) % 4 == 3) {
                    //第4个点
                    mLineCoords[i] = mPointCoords[(i - 5) / 2 + 3];
                }
            } else if (i == mLineCoords.length / 2 - 2) {
                //高温区的最后一个（x坐标）
                mLineCoords[i] = mPointCoords[(i - 2) / 2 + 2];
            } else if (i == mLineCoords.length / 2 - 1) {
                //高温区的最后一个（y坐标）
                mLineCoords[i] = mPointCoords[(i - 3) / 2 + 3];
            } else if (i == mLineCoords.length / 2) {
                //低温区的第一个（x坐标）
                mLineCoords[i] = mPointCoords[(i - 4) / 2 + 4];
            } else if (i == mLineCoords.length / 2 + 1) {
                //低温区的第一个（y坐标）
                mLineCoords[i] = mPointCoords[(i - 5) / 2 + 5];
            } else if (i >= mLineCoords.length / 2 + 2 && i <= mLineCoords.length - 3) {
                //低温区，需要重复取点
                //每4个点为一组
                if ((i - 2) % 4 == 0) {
                    //第1个点
                    mLineCoords[i] = mPointCoords[(i - 2) / 2 + 4];
                } else if ((i - 2) % 4 == 1) {
                    //第2个点
                    mLineCoords[i] = mPointCoords[(i - 3) / 2 + 5];
                } else if ((i - 2) % 4 == 2) {
                    //第3个点
                    mLineCoords[i] = mPointCoords[(i - 4) / 2 + 4];
                } else if ((i - 2) % 4 == 3) {
                    //第4个点
                    mLineCoords[i] = mPointCoords[(i - 5) / 2 + 5];
                }
            } else if (i == mLineCoords.length - 2) {
                //最后一个（低温区的最后一个）（x坐标）
                mLineCoords[i] = mPointCoords[(i - 2) / 2 + 4];
            } else if (i == mLineCoords.length - 1) {
                //最后一个（低温区的最后一个）（y坐标）
                mLineCoords[i] = mPointCoords[(i - 3) / 2 + 5];
            }
        }
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    /*public String getExampleString() {
        return mExampleString;
    }*/

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    /*public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }*/

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    /*public int getExampleColor() {
        return mExampleColor;
    }*/

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    /*public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }*/

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    /*public float getExampleDimension() {
        return mExampleDimension;
    }*/

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    /*public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }*/

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    /*public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }*/

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    /*public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }*/
}
