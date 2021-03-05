package net.zackzhang.code.haze.common.model.local

import androidx.room.TypeConverter
import net.zackzhang.code.haze.common.util.DateTimeUtils
import net.zackzhang.code.haze.common.util.NumberUtils
import java.time.*

object Converters {

    @TypeConverter
    fun dateToString(date: LocalDate?) = date?.toString()

    @TypeConverter
    fun stringToDate(date: String?) = DateTimeUtils.parseDate(date)

    @TypeConverter
    fun timeToString(time: LocalTime?) = time?.toString()

    @TypeConverter
    fun stringToTime(time: String?) = DateTimeUtils.parseTime(time)

    @TypeConverter
    fun dateTimeToString(dateTime: ZonedDateTime?) = dateTime?.toString()

    @TypeConverter
    fun stringToDateTime(dateTime: String?) = DateTimeUtils.parseDateTime(dateTime)

    @TypeConverter
    fun timeZoneToString(timeZone: ZoneId?) = timeZone?.toString()

    @TypeConverter
    fun stringToTimeZone(timeZone: String?) = DateTimeUtils.parseTimeZone(timeZone)

    @TypeConverter
    fun utcOffsetToString(utcOffset: ZoneOffset?) = utcOffset?.toString()

    @TypeConverter
    fun stringToUtcOffset(utcOffset: String?) = DateTimeUtils.parseUtcOffset(utcOffset)

    @TypeConverter
    fun intRangeToString(intRange: IntRange?) = NumberUtils.presentIntRange(intRange)

    @TypeConverter
    fun stringToIntRange(intRange: String?) = NumberUtils.parseIntRange(intRange)
}