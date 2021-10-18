package net.zackzhang.code.haze.common.util

import net.zackzhang.code.haze.HazeApplication
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.util.ResourceUtils.getFormattedQuantityString
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAccessor
import java.time.zone.ZoneRulesException

object DateTimeUtils {

    /**
     * yyyy-MM-dd HH:mm 格式
     */
    private val FORMATTER by lazy {
        DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral(' ')
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .toFormatter()!!
    }

    fun parseDate(date: String?) = try {
        date?.let { LocalDate.parse(it) }
    } catch (e: DateTimeParseException) {
        e.printStackTrace()
        null
    }

    fun parseTime(time: String?) = try {
        time?.let { LocalTime.parse(it) }
    } catch (e: DateTimeParseException) {
        e.printStackTrace()
        null
    }

    fun parseDateTime(dateTime: String?) = try {
        dateTime?.let { ZonedDateTime.parse(it) }
    } catch (e: DateTimeParseException) {
        e.printStackTrace()
        null
    }

    fun parseTimeZone(timeZone: String?) = try {
        timeZone?.let { ZoneId.of(it) }
    } catch (e: DateTimeException) {
        e.printStackTrace()
        null
    } catch (e: ZoneRulesException) {
        e.printStackTrace()
        null
    }

    fun parseUtcOffset(utcOffset: String?) = try {
        utcOffset?.let { ZoneOffset.of(it) }
    } catch (e: DateTimeException) {
        e.printStackTrace()
        null
    }

    /**
     * 格式化为 yyyy-MM-dd HH:mm
     */
    fun TemporalAccessor.format() = FORMATTER.format(this)!!

    /**
     * 转换为相对当前时间描述的字符串
     * e.g. 4 小时前
     */
    fun ZonedDateTime.toPrettifiedRelativeToNow(): String {
        val duration = Duration.between(this, ZonedDateTime.now())
        val context = HazeApplication.context
        return when {
            // 该时间晚于当前设备时间，应该是设备时间调快了
            duration.isNegative -> context.getString(
                R.string.datetime_prettified_relative_to_now_negative_format,
                format()
            )
            // 距离当前已不低于 1 天，显示"一天前"
            duration.toHours() >= 24 -> context.getString(R.string.datetime_prettified_relative_to_now_day)
            // 距离当前低于 1 天，但不低于 1 小时，显示具体小时数
            duration.toMinutes() >= 60 -> context.getFormattedQuantityString(
                R.plurals.datetime_prettified_relative_to_now_hour_format,
                duration.toHours().toInt()
            )
            // 距离当前低于 1 小时，但不低于 1 分钟，显示具体分钟数
            duration.seconds >= 60 -> context.getFormattedQuantityString(
                R.plurals.datetime_prettified_relative_to_now_minute_format,
                duration.toMinutes().toInt()
            )
            // 距离当前低于 1 分钟，显示"刚才"
            else -> context.getString(R.string.datetime_prettified_relative_to_now_second)
        }
    }
}