package net.zackzhang.code.haze.common.util

import java.time.*
import java.time.format.DateTimeParseException
import java.time.zone.ZoneRulesException

object DateTimeUtils {

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
}