package net.zackzhang.code.haze.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import net.zackzhang.code.haze.R

class TemperatureTrendChartView : View {

    private val MARGIN_TOP = 30
    private val SPACING_WEEK_CONDITION = 10
    private val SPACING_CONDITION_TEMP = 80
    private val MARGIN_BOTTOM = 80

    private val STROKE_WIDTH_POINT = 15f
    private val STROKE_WIDTH_POINT_EDGE = 25f
    private val STROKE_WIDTH_LINE = 5f

    private val TEXT_BASE_OFFSET = 40f

    // 星期数组
    private lateinit var mWeekArray: Array<String>
    // 天气状况数组
    private lateinit var mConditionArray: Array<String>
    // 温度最大值的数组
    private lateinit var mTemperatureMaxArray: IntArray
    // 温度最小值的数组
    private lateinit var mTemperatureMinArray: IntArray

    // 公用的x坐标数组
    private lateinit var mXCoordinates: FloatArray
    // 温度点坐标数组
    private lateinit var mPointCoordinates: FloatArray
    // 温度趋势连线坐标数组
    private lateinit var mLineCoordinates: FloatArray

    // 是否已传入温度数据
    private var mHasTemperatureData = false

    private val mTextPaint = TextPaint()
    private val mPaint = Paint()

    private var mWeekTextSize = 0f
    private var mWeekTextColor = Color.BLACK
    private var mConditionTextSize = 0f
    private var mConditionTextColor = Color.BLACK
    private var mTempTextSize = 0f
    private var mTempTextColor = Color.BLACK
    private var mTempPointColor = Color.BLACK
    private var mTempPointEdgeColor = Color.GRAY
    private var mTempTrendLineColor = Color.BLACK

    private var mWeekBaseline = 0f
    private var mConditionBaseline = 0f

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {

        loadAttrs(attrs, defStyle)

        //设置画笔模式为填充
        mTextPaint.style = Paint.Style.FILL
        //设置抗锯齿
        mTextPaint.isAntiAlias = true
        //设置对齐方式为中心对齐
        mTextPaint.textAlign = Paint.Align.CENTER

        //设置画笔模式为填充
        mPaint.style = Paint.Style.FILL
        //设置画笔形状
        mPaint.strokeCap = Paint.Cap.ROUND
        //设置抗锯齿
        mPaint.isAntiAlias = true
    }

    // 加载自定义的属性
    private fun loadAttrs(attrs: AttributeSet?, defStyle: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TemperatureTrendChartView, defStyle, 0)
        mWeekTextSize = ta.getDimension(R.styleable.TemperatureTrendChartView_week_text_size, mWeekTextSize)
        mWeekTextColor = ta.getColor(R.styleable.TemperatureTrendChartView_week_text_color, mWeekTextColor)
        mConditionTextSize = ta.getDimension(R.styleable.TemperatureTrendChartView_condition_text_size, mConditionTextSize)
        mConditionTextColor = ta.getColor(R.styleable.TemperatureTrendChartView_condition_text_color, mConditionTextColor)
        mTempTextSize = ta.getDimension(R.styleable.TemperatureTrendChartView_temp_text_size, mTempTextSize)
        mTempTextColor = ta.getColor(R.styleable.TemperatureTrendChartView_temp_text_color, mTempTextColor)
        mTempPointColor = ta.getColor(R.styleable.TemperatureTrendChartView_temp_point_color, mTempPointColor)
        mTempPointEdgeColor = ta.getColor(R.styleable.TemperatureTrendChartView_temp_point_edge_color, mTempPointEdgeColor)
        mTempTrendLineColor = ta.getColor(R.styleable.TemperatureTrendChartView_temp_trend_line_color, mTempTrendLineColor)
        ta.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // 没有温度数据，不进行绘制
        if (!mHasTemperatureData) return

        calculateXCoordinates()

        drawWeeks(canvas)

        drawConditions(canvas)

        calculatePointCoordinates()

        drawTemperatureValues(canvas)

        calculateLineCoordinates()

        drawTemperatureTrend(canvas)
    }

    /**
     * 设置要显示的数据，四个数组的长度必须相等
     * @param weekArray 星期数组
     * @param conditionArray 天气状况数组
     * @param temperatureMaxArray 温度最大值的数组
     * @param temperatureMinArray 温度最小值的数组
     */
    fun setTemperatureData(weekArray: Array<String>, conditionArray: Array<String>, temperatureMaxArray: IntArray, temperatureMinArray: IntArray) {
        if (weekArray.size != conditionArray.size || conditionArray.size != temperatureMaxArray.size || temperatureMaxArray.size != temperatureMinArray.size) {
            throw IllegalArgumentException("Length of four arrays are not equal")
        }
        
        mWeekArray = weekArray
        mConditionArray = conditionArray
        mTemperatureMaxArray = temperatureMaxArray
        mTemperatureMinArray = temperatureMinArray
        
        //初始化x坐标数组
        mXCoordinates = FloatArray(mTemperatureMaxArray.size)
        //初始化温度点坐标数组
        mPointCoordinates = FloatArray((mTemperatureMaxArray.size + mTemperatureMinArray.size) * 2)
        //初始化温度趋势连线坐标数组
        mLineCoordinates = FloatArray((mTemperatureMaxArray.size + mTemperatureMinArray.size) * 4 - 8)

        mHasTemperatureData = true
        
        //重新绘制
        invalidate()
    }

    // 计算公用的x坐标
    private fun calculateXCoordinates() {
        val startX = paddingLeft
        //每个区间的宽
        val sectionWidth = ((width - paddingLeft - paddingRight) / mTemperatureMaxArray.size).toFloat()
        //计算x坐标
        for (i in mXCoordinates.indices) {
            mXCoordinates[i] = startX.toFloat() + sectionWidth / 2 + sectionWidth * i
        }
    }

    // 画星期
    private fun drawWeeks(canvas: Canvas) {
        //设置字体大小
        mTextPaint.textSize = mWeekTextSize
        //设置字体颜色
        mTextPaint.color = mWeekTextColor
        //文本基线y坐标
        mWeekBaseline = paddingTop + MARGIN_TOP - mTextPaint.ascent()
        //画星期
        for (i in mXCoordinates.indices) {
            canvas.drawText(mWeekArray[i], mXCoordinates[i], mWeekBaseline, mTextPaint)
        }
    }

    // 画天气状况
    private fun drawConditions(canvas: Canvas) {
        //上一行（星期）的底部坐标
        val lastLineBottom = mWeekBaseline + mTextPaint.descent()
        mTextPaint.textSize = mConditionTextSize
        mTextPaint.color = mConditionTextColor
        mConditionBaseline = lastLineBottom + SPACING_WEEK_CONDITION - mTextPaint.ascent()
        //画天气状况
        for (i in mXCoordinates.indices) {
            canvas.drawText(mConditionArray[i], mXCoordinates[i], mConditionBaseline, mTextPaint)
        }
    }

    // 计算温度点的坐标
    private fun calculatePointCoordinates() {
        //最高温度
        val maxTemp = mTemperatureMaxArray.maxOrNull()!!
        //最低温度
        val minTemp = mTemperatureMinArray.minOrNull()!!
        val lastLineBottom = mConditionBaseline + mTextPaint.descent()
        val startY = lastLineBottom + SPACING_CONDITION_TEMP
        //行数量
        val rowCount = maxTemp - minTemp + 1
        //每个区间的高
        val sectionHeight = (height.toFloat() - startY - paddingBottom.toFloat() - MARGIN_BOTTOM.toFloat()) / rowCount
        //计算所有坐标
        for (i in mPointCoordinates.indices) {
            if (i % 2 == 0) {
                //偶数：x坐标
                if (i < mTemperatureMaxArray.size * 2) {
                    //高温部分
                    mPointCoordinates[i] = mXCoordinates[i / 2]//startX + sectionWidth / 2 + sectionWidth * (i / 2);
                } else {
                    //低温部分
                    mPointCoordinates[i] = mXCoordinates[(i - mTemperatureMaxArray.size * 2) / 2]//mPointCoordinates[i - mTemperatureMaxArray.length * 2];
                }
            } else {
                //奇数：y坐标（在屏幕上由上至下遍历）
                for (j in 0 until rowCount) {
                    if (i < mTemperatureMaxArray.size * 2) {
                        //高温部分
                        if (maxTemp - j == mTemperatureMaxArray[(i - 1) / 2]) {
                            //说明通过mMaxTempArray取出的温度在第j个区间
                            mPointCoordinates[i] = startY + sectionHeight / 2 + sectionHeight * j
                            break
                        }
                    } else {
                        //低温部分
                        if (maxTemp - j == mTemperatureMinArray[(i - mTemperatureMaxArray.size * 2 - 1) / 2]) {
                            //说明通过mMinTempArray取出的温度在第j个区间
                            mPointCoordinates[i] = startY + sectionHeight / 2 + sectionHeight * j
                            break
                        }
                    }
                }
            }
        }
    }

    // 画温度值
    private fun drawTemperatureValues(canvas: Canvas) {
        mTextPaint.textSize = mTempTextSize
        mTextPaint.color = mTempTextColor
        //文本高度
        val textHeight = mTextPaint.descent() - mTextPaint.ascent()
        //高、低温文本相对点的偏移（文本是以基线为准的）
        val maxTempTextOffset = TEXT_BASE_OFFSET - textHeight / 2 + mTextPaint.descent()
        val minTempTextOffset = TEXT_BASE_OFFSET + textHeight / 2 - mTextPaint.descent()
        //画温度值
        for (i in mTemperatureMaxArray.indices) {
            //高温值
            canvas.drawText(
                    mTemperatureMaxArray[i].toString(),
                    mPointCoordinates[i * 2],
                    mPointCoordinates[i * 2 + 1] - maxTempTextOffset,
                    mTextPaint
            )
            //低温值
            canvas.drawText(
                    mTemperatureMinArray[i].toString(),
                    mPointCoordinates[mTemperatureMaxArray.size * 2 + i * 2],
                    mPointCoordinates[mTemperatureMaxArray.size * 2 + i * 2 + 1] + minTempTextOffset,
                    mTextPaint
            )
        }
    }

    // 计算温度趋势连线的坐标
    private fun calculateLineCoordinates() {
        for (i in mLineCoordinates.indices) {
            if (i == 0 || i == 1) {
                //第一个（高温区的第一个）
                mLineCoordinates[i] = mPointCoordinates[i]
            } else if (i >= 2 && i <= mLineCoordinates.size / 2 - 3) {
                //高温区，需要重复取点
                //每4个点为一组
                when {
                    //第1个点
                    (i - 2) % 4 == 0 -> mLineCoordinates[i] = mPointCoordinates[(i - 2) / 2 + 2]
                    //第2个点
                    (i - 2) % 4 == 1 -> mLineCoordinates[i] = mPointCoordinates[(i - 3) / 2 + 3]
                    //第3个点
                    (i - 2) % 4 == 2 -> mLineCoordinates[i] = mPointCoordinates[(i - 4) / 2 + 2]
                    //第4个点
                    (i - 2) % 4 == 3 -> mLineCoordinates[i] = mPointCoordinates[(i - 5) / 2 + 3]
                }
            } else if (i == mLineCoordinates.size / 2 - 2) {
                //高温区的最后一个（x坐标）
                mLineCoordinates[i] = mPointCoordinates[(i - 2) / 2 + 2]
            } else if (i == mLineCoordinates.size / 2 - 1) {
                //高温区的最后一个（y坐标）
                mLineCoordinates[i] = mPointCoordinates[(i - 3) / 2 + 3]
            } else if (i == mLineCoordinates.size / 2) {
                //低温区的第一个（x坐标）
                mLineCoordinates[i] = mPointCoordinates[(i - 4) / 2 + 4]
            } else if (i == mLineCoordinates.size / 2 + 1) {
                //低温区的第一个（y坐标）
                mLineCoordinates[i] = mPointCoordinates[(i - 5) / 2 + 5]
            } else if (i >= mLineCoordinates.size / 2 + 2 && i <= mLineCoordinates.size - 3) {
                //低温区，需要重复取点
                //每4个点为一组
                when {
                    //第1个点
                    (i - 2) % 4 == 0 -> mLineCoordinates[i] = mPointCoordinates[(i - 2) / 2 + 4]
                    //第2个点
                    (i - 2) % 4 == 1 -> mLineCoordinates[i] = mPointCoordinates[(i - 3) / 2 + 5]
                    //第3个点
                    (i - 2) % 4 == 2 -> mLineCoordinates[i] = mPointCoordinates[(i - 4) / 2 + 4]
                    //第4个点
                    (i - 2) % 4 == 3 -> mLineCoordinates[i] = mPointCoordinates[(i - 5) / 2 + 5]
                }
            } else if (i == mLineCoordinates.size - 2) {
                //最后一个（低温区的最后一个）（x坐标）
                mLineCoordinates[i] = mPointCoordinates[(i - 2) / 2 + 4]
            } else if (i == mLineCoordinates.size - 1) {
                //最后一个（低温区的最后一个）（y坐标）
                mLineCoordinates[i] = mPointCoordinates[(i - 3) / 2 + 5]
            }
        }
    }

    // 画温度点与趋势
    private fun drawTemperatureTrend(canvas: Canvas) {
        //设置画笔颜色
        mPaint.color = mTempPointEdgeColor
        //设置画笔宽度
        mPaint.strokeWidth = STROKE_WIDTH_POINT_EDGE
        //画温度点边缘
        canvas.drawPoints(mPointCoordinates, mPaint)

        mPaint.color = mTempPointColor
        mPaint.strokeWidth = STROKE_WIDTH_POINT
        //画温度点
        canvas.drawPoints(mPointCoordinates, mPaint)

        mPaint.color = mTempTrendLineColor
        mPaint.strokeWidth = STROKE_WIDTH_LINE
        //画温度连线
        canvas.drawLines(mLineCoordinates, mPaint)
    }
}
