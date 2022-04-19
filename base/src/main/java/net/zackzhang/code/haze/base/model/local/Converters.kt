package net.zackzhang.code.haze.base.model.local

import androidx.room.TypeConverter
import net.zackzhang.code.haze.base.util.*
import java.time.*

object Converters {

    @TypeConverter
    fun dateToString(date: LocalDate?) = date?.toString()

    @TypeConverter
    fun stringToDate(date: String?) = parseDate(date)

    @TypeConverter
    fun timeToString(time: LocalTime?) = time?.toString()

    @TypeConverter
    fun stringToTime(time: String?) = parseTime(time)

    @TypeConverter
    fun dateTimeToString(dateTime: ZonedDateTime?) = dateTime?.toString()

    @TypeConverter
    fun stringToDateTime(dateTime: String?) = parseDateTime(dateTime)

    @TypeConverter
    fun timeZoneToString(timeZone: ZoneId?) = timeZone?.toString()

    @TypeConverter
    fun stringToTimeZone(timeZone: String?) = parseTimeZone(timeZone)

    @TypeConverter
    fun utcOffsetToString(utcOffset: ZoneOffset?) = utcOffset?.toString()

    @TypeConverter
    fun stringToUtcOffset(utcOffset: String?) = parseUtcOffset(utcOffset)

    @TypeConverter
    fun intRangeToString(intRange: IntRange?) = presentIntRange(intRange)

    @TypeConverter
    fun stringToIntRange(intRange: String?) = parseIntRange(intRange)
}