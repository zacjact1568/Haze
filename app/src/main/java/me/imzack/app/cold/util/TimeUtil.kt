package me.imzack.app.cold.util

import android.text.format.DateFormat
import me.imzack.app.cold.App
import me.imzack.app.cold.R
import me.imzack.app.cold.common.Constant
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

    fun parseTime(time: String): Long {
        try {
            return SimpleDateFormat(Constant.TIME_FORMAT, Locale.getDefault()).parse(time).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0L
    }

    fun parseDate(time: String): Long {
        try {
            return SimpleDateFormat(Constant.DATE_FORMAT, Locale.getDefault()).parse(time).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0L
    }

    fun formatTime(time: Long): String {
        // TODO 添加具体时间描述，例如4分钟之前
        val date = Date(time)
        val dateStr = DateFormat.getMediumDateFormat(App.context).format(date)
        val timeStr = DateFormat.getTimeFormat(App.context).format(date)
        return "$dateStr $timeStr"
    }

    /** 产生表示星期的字符串数组 */
    fun getWeeks(from: Long, length: Int): Array<String> {

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = from

        val resources = App.context.resources

        var week: Int

        return Array(length) {
            when (it) {
                0 -> resources.getString(R.string.text_today)
                1 -> resources.getString(R.string.text_tomorrow)
                else -> {
                    // 增加
                    calendar.add(Calendar.DATE, it)
                    week = calendar.get(Calendar.DAY_OF_WEEK)
                    // 恢复
                    calendar.add(Calendar.DATE, -it)
                    resources.getString(when (week) {
                        Calendar.MONDAY -> R.string.text_monday
                        Calendar.TUESDAY -> R.string.text_tuesday
                        Calendar.WEDNESDAY -> R.string.text_wednesday
                        Calendar.THURSDAY -> R.string.text_thursday
                        Calendar.FRIDAY -> R.string.text_friday
                        Calendar.SATURDAY -> R.string.text_saturday
                        Calendar.SUNDAY -> R.string.text_sunday
                        else -> throw IllegalArgumentException("No week days named \"$week\"")
                    })
                }
            }
        }
    }
}
