package com.zack.enderweather.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.zack.enderweather.R;

public class TemperatureTrendChartView extends View {

    private static final int MARGIN_TOP = 30;
    private static final int SPACING_WEEK_CONDITION = 10;
    private static final int SPACING_CONDITION_TEMP = 80;
    private static final int MARGIN_BOTTOM = 80;

    private static final float STROKE_WIDTH_POINT = 15f;
    private static final float STROKE_WIDTH_POINT_EDGE = 25f;
    private static final float STROKE_WIDTH_LINE = 5f;

    private static final float TEXT_BASE_OFFSET = 40f;

    /** 星期数组 */
    private String[] mWeekArray;
    /** 天气状况数组 */
    private String[] mConditionArray;
    /** 温度最大值的数组 */
    private int[] mMaxTempArray;
    /** 温度最小值的数组 */
    private int[] mMinTempArray;

    /** 公用的x坐标数组 */
    private float[] mXCoords;
    /** 温度点坐标数组 */
    private float[] mPointCoords;
    /** 温度趋势连线坐标数组 */
    private float[] mLineCoords;

    private TextPaint mTextPaint;
    private Paint mPaint;

    private float mWeekTextSize = 0f;
    private int mWeekTextColor = Color.BLACK;
    private float mConditionTextSize = 0f;
    private int mConditionTextColor = Color.BLACK;
    private float mTempTextSize = 0f;
    private int mTempTextColor = Color.BLACK;
    private int mTempPointColor = Color.BLACK;
    private int mTempPointEdgeColor = Color.GRAY;
    private int mTempTrendLineColor = Color.BLACK;

    private float mWeekBaseline;
    private float mConditionBaseline;

    /*private float mTextWidth;
    private String mExampleString;
    private int mExampleColor = Color.RED;
    private Drawable mExampleDrawable;*/

    public TemperatureTrendChartView(Context context) {
        super(context);
        init(null, 0);
    }

    public TemperatureTrendChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TemperatureTrendChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        loadAttrs(attrs, defStyle);

        mTextPaint = new TextPaint();
        //设置画笔模式为填充
        mTextPaint.setStyle(Paint.Style.FILL);
        //设置抗锯齿
        mTextPaint.setAntiAlias(true);
        //设置对齐方式为中心对齐
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mPaint = new Paint();
        //设置画笔模式为填充
        mPaint.setStyle(Paint.Style.FILL);
        //设置画笔形状
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //设置抗锯齿
        mPaint.setAntiAlias(true);

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

    /** 加载自定义的属性 */
    private void loadAttrs(AttributeSet attrs, int defStyle) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TemperatureTrendChartView, defStyle, 0);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with values that should fall on pixel boundaries.
        mWeekTextSize = ta.getDimension(R.styleable.TemperatureTrendChartView_week_text_size, mWeekTextSize);
        mWeekTextColor = ta.getColor(R.styleable.TemperatureTrendChartView_week_text_color, mWeekTextColor);
        mConditionTextSize = ta.getDimension(R.styleable.TemperatureTrendChartView_condition_text_size, mConditionTextSize);
        mConditionTextColor = ta.getColor(R.styleable.TemperatureTrendChartView_condition_text_color, mConditionTextColor);
        mTempTextSize = ta.getDimension(R.styleable.TemperatureTrendChartView_temp_text_size, mTempTextSize);
        mTempTextColor = ta.getColor(R.styleable.TemperatureTrendChartView_temp_text_color, mTempTextColor);
        mTempPointColor = ta.getColor(R.styleable.TemperatureTrendChartView_temp_point_color, mTempPointColor);
        mTempPointEdgeColor = ta.getColor(R.styleable.TemperatureTrendChartView_temp_point_edge_color, mTempPointEdgeColor);
        mTempTrendLineColor = ta.getColor(R.styleable.TemperatureTrendChartView_temp_trend_line_color, mTempTrendLineColor);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mMaxTempArray == null && mMinTempArray == null) {
            //当没有数据时，不进行绘制
            return;
        }

        calculateXCoords();

        drawWeeks(canvas);

        drawConditions(canvas);

        calculatePointCoords();

        drawTempValues(canvas);

        calculateLineCoords();

        drawTempTrend(canvas);

        /*// Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }*/
    }

    /**
     * 设置要显示的数据
     * @param weekArray 星期数组
     * @param conditionArray 天气状况数组
     * @param maxTempArray 温度最大值的数组
     * @param minTempArray 温度最小值的数组
     */
    public void setTempArray(String[] weekArray, String[] conditionArray, int[] maxTempArray, int[] minTempArray) {
        if (maxTempArray.length != minTempArray.length) {
            throw new RuntimeException("Lengths of max and min temperature array are not equal");
        }
        mWeekArray = weekArray;
        mConditionArray = conditionArray;
        mMaxTempArray = maxTempArray;
        mMinTempArray = minTempArray;
        //初始化x坐标数组
        mXCoords = new float[mMaxTempArray.length];
        //初始化温度点坐标数组
        mPointCoords = new float[(mMaxTempArray.length + mMinTempArray.length) * 2];
        //初始化温度趋势连线坐标数组
        mLineCoords = new float[(mMaxTempArray.length + mMinTempArray.length) * 4 - 8];
        //重新绘制
        invalidate();
    }

    /** 计算公用的x坐标 */
    private void calculateXCoords() {
        int startX = getPaddingLeft();
        //每个区间的宽
        float sectionWidth = (getWidth() - getPaddingLeft() - getPaddingRight()) / mMaxTempArray.length;
        //计算x坐标
        for (int i = 0; i < mXCoords.length; i++) {
            mXCoords[i] = startX + sectionWidth / 2 + sectionWidth * i;
        }
    }

    /** 画星期 */
    private void drawWeeks(Canvas canvas) {
        //设置字体大小
        mTextPaint.setTextSize(mWeekTextSize);
        //设置字体颜色
        mTextPaint.setColor(mWeekTextColor);
        //文本基线y坐标
        mWeekBaseline = getPaddingTop() + MARGIN_TOP - mTextPaint.ascent();
        //画星期
        for (int i = 0; i < mXCoords.length; i++) {
            canvas.drawText(mWeekArray[i], mXCoords[i], mWeekBaseline, mTextPaint);
        }
    }

    /** 画天气状况 */
    private void drawConditions(Canvas canvas) {
        //上一行（星期）的底部坐标
        float lastLineBottom = mWeekBaseline + mTextPaint.descent();
        mTextPaint.setTextSize(mConditionTextSize);
        mTextPaint.setColor(mConditionTextColor);
        mConditionBaseline = lastLineBottom + SPACING_WEEK_CONDITION - mTextPaint.ascent();
        //画天气状况
        for (int i = 0; i < mXCoords.length; i++) {
            canvas.drawText(mConditionArray[i], mXCoords[i], mConditionBaseline, mTextPaint);
        }
    }

    /** 计算温度点的坐标 */
    private void calculatePointCoords() {
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
        float lastLineBottom = mConditionBaseline + mTextPaint.descent();
        float startY = lastLineBottom + SPACING_CONDITION_TEMP;
        //行数量
        int rowCount = maxTemp - minTemp + 1;
        //每个区间的高
        float sectionHeight = (getHeight() - startY - getPaddingBottom() - MARGIN_BOTTOM) / rowCount;
        //计算所有坐标
        for (int i = 0; i < mPointCoords.length; i++) {
            if (i % 2 == 0) {
                //偶数：x坐标
                if (i < mMaxTempArray.length * 2) {
                    //高温部分
                    mPointCoords[i] = mXCoords[i / 2];//startX + sectionWidth / 2 + sectionWidth * (i / 2);
                } else {
                    //低温部分
                    mPointCoords[i] = mXCoords[(i - mMaxTempArray.length * 2) / 2];//mPointCoords[i - mMaxTempArray.length * 2];
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

    /** 画温度值 */
    private void drawTempValues(Canvas canvas) {
        mTextPaint.setTextSize(mTempTextSize);
        mTextPaint.setColor(mTempTextColor);
        //文本高度
        float textHeight = mTextPaint.descent() - mTextPaint.ascent();
        //高、低温文本相对点的偏移（文本是以基线为准的）
        float maxTempTextOffset = TEXT_BASE_OFFSET - textHeight / 2 + mTextPaint.descent();
        float minTempTextOffset = TEXT_BASE_OFFSET + textHeight / 2 - mTextPaint.descent();
        //画温度值
        for (int i = 0; i < mMaxTempArray.length; i++) {
            //高温值
            canvas.drawText(
                    String.valueOf(mMaxTempArray[i]),
                    mPointCoords[i * 2],
                    //paddingLeft + (contentWidth - mTextWidth) / 2,
                    mPointCoords[i * 2 + 1] - maxTempTextOffset,
                    //paddingTop + (contentHeight + mTextHeight) / 2,
                    mTextPaint
            );
            //低温值
            canvas.drawText(
                    String.valueOf(mMinTempArray[i]),
                    mPointCoords[mMaxTempArray.length * 2 + i * 2],
                    mPointCoords[mMaxTempArray.length * 2 + i * 2 + 1] + minTempTextOffset,
                    mTextPaint
            );
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

    /** 画温度点与趋势 */
    private void drawTempTrend(Canvas canvas) {
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
