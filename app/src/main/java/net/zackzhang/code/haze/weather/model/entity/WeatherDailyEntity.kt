package net.zackzhang.code.haze.weather.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.google.gson.annotations.SerializedName
import net.zackzhang.code.haze.city.model.entity.CityEntity
import net.zackzhang.code.haze.common.constant.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime

@Entity(tableName = WEATHER_DAILY, foreignKeys = [ForeignKey(
    entity = CityEntity::class,
    parentColumns = [ID],
    childColumns = [CITY_ID],
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE,
)], primaryKeys = [CITY_ID, DATE])
data class WeatherDailyEntity(
    @ColumnInfo(name = CITY_ID)
    var cityId: String,
    @ColumnInfo(name = UPDATED_AT)
    var updatedAt: ZonedDateTime?,
    @SerializedName("fxDate")
    @ColumnInfo(name = DATE)
    val date: LocalDate,
    @SerializedName("sunrise")
    @ColumnInfo(name = SUNRISE)
    val sunrise: LocalTime?,
    @SerializedName("sunset")
    @ColumnInfo(name = SUNSET)
    val sunset: LocalTime?,
    @SerializedName("moonrise")
    @ColumnInfo(name = MOONRISE)
    val moonrise: LocalTime?,
    @SerializedName("moonset")
    @ColumnInfo(name = MOONSET)
    val moonset: LocalTime?,
    @SerializedName("moonPhase")
    @ColumnInfo(name = MOON_PHASE)
    val moonPhase: String?,
    @SerializedName("tempMax")
    @ColumnInfo(name = TEMPERATURE_MAX)
    val temperatureMax: Int?,
    @SerializedName("tempMin")
    @ColumnInfo(name = TEMPERATURE_MIN)
    val temperatureMin: Int?,
    @SerializedName("iconDay")
    @ColumnInfo(name = CONDITION_CODE_DAY)
    val conditionCodeDay: Int?,
    @SerializedName("textDay")
    @ColumnInfo(name = CONDITION_NAME_DAY)
    val conditionNameDay: String?,
    @SerializedName("iconNight")
    @ColumnInfo(name = CONDITION_CODE_NIGHT)
    val conditionCodeNight: Int?,
    @SerializedName("textNight")
    @ColumnInfo(name = CONDITION_NAME_NIGHT)
    val conditionNameNight: String?,
    @SerializedName("wind360Day")
    @ColumnInfo(name = WIND_ANGLE_DAY)
    val windAngleDay: Int?,
    @SerializedName("windDirDay")
    @ColumnInfo(name = WIND_DIRECTION_DAY)
    val windDirectionDay: String?,
    @SerializedName("windScaleDay")
    @ColumnInfo(name = WIND_SCALE_DAY)
    val windScaleDay: IntRange?,
    @SerializedName("windSpeedDay")
    @ColumnInfo(name = WIND_SPEED_DAY)
    val windSpeedDay: Int?,
    @SerializedName("wind360Night")
    @ColumnInfo(name = WIND_ANGLE_NIGHT)
    val windAngleNight: Int?,
    @SerializedName("windDirNight")
    @ColumnInfo(name = WIND_DIRECTION_NIGHT)
    val windDirectionNight: String?,
    @SerializedName("windScaleNight")
    @ColumnInfo(name = WIND_SCALE_NIGHT)
    val windScaleNight: IntRange?,
    @SerializedName("windSpeedNight")
    @ColumnInfo(name = WIND_SPEED_NIGHT)
    val windSpeedNight: Int?,
    @SerializedName("humidity")
    @ColumnInfo(name = HUMIDITY)
    val humidity: Int?,
    @SerializedName("precip")
    @ColumnInfo(name = PRECIPITATION)
    val precipitation: Float?,
    @SerializedName("pressure")
    @ColumnInfo(name = PRESSURE)
    val pressure: Int?,
    @SerializedName("vis")
    @ColumnInfo(name = VISIBILITY)
    val visibility: Int?,
    @SerializedName("cloud")
    @ColumnInfo(name = CLOUD)
    val cloud: Int?,
    @SerializedName("uvIndex")
    @ColumnInfo(name = UV_INDEX)
    val uvIndex: Int?,
)