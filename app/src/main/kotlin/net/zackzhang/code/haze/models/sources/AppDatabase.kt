package net.zackzhang.code.haze.models.sources

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import net.zackzhang.code.haze.utils.appName
import net.zackzhang.code.haze.utils.parseDate
import net.zackzhang.code.haze.utils.parseDateTime
import net.zackzhang.code.haze.utils.parseIntRange
import net.zackzhang.code.haze.utils.parseTime
import net.zackzhang.code.haze.utils.parseTimeZone
import net.zackzhang.code.haze.utils.parseUtcOffset
import net.zackzhang.code.haze.utils.presentIntRange
import net.zackzhang.code.util.AppContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

val AppDatabaseInstance = Room.databaseBuilder(
    AppContext,
    AppDatabase::class.java,
    "$appName.db",
).build()

@Database(
    entities = [
        CityEntity::class,
        WeatherHourlyEntity::class,
        WeatherDailyEntity::class,
        AirNowEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    abstract fun weatherDao(): WeatherDao
}

object AppTypeConverters {

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