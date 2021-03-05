package net.zackzhang.code.haze.weather.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.google.gson.annotations.SerializedName
import net.zackzhang.code.haze.city.model.entity.CityEntity
import net.zackzhang.code.haze.common.Constants
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime

@Entity(tableName = Constants.WEATHER_DAILY, foreignKeys = [ForeignKey(
    entity = CityEntity::class,
    parentColumns = [Constants.ID],
    childColumns = [Constants.CITY_ID],
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE,
)], primaryKeys = [Constants.CITY_ID, Constants.DATE])
data class WeatherDailyEntity(
    @ColumnInfo(name = Constants.CITY_ID)
    var cityId: String,
    @ColumnInfo(name = Constants.UPDATED_AT)
    var updatedAt: ZonedDateTime?,
    @SerializedName("fxDate")
    @ColumnInfo(name = Constants.DATE)
    val date: LocalDate,
    @SerializedName("sunrise")
    @ColumnInfo(name = Constants.SUNRISE)
    val sunrise: LocalTime?,
    @SerializedName("sunset")
    @ColumnInfo(name = Constants.SUNSET)
    val sunset: LocalTime?,
    @SerializedName("moonrise")
    @ColumnInfo(name = Constants.MOONRISE)
    val moonrise: LocalTime?,
    @SerializedName("moonset")
    @ColumnInfo(name = Constants.MOONSET)
    val moonset: LocalTime?,
    @SerializedName("moonPhase")
    @ColumnInfo(name = Constants.MOON_PHASE)
    val moonPhase: String?,
    @SerializedName("tempMax")
    @ColumnInfo(name = Constants.TEMPERATURE_MAX)
    val temperatureMax: Int?,
    @SerializedName("tempMin")
    @ColumnInfo(name = Constants.TEMPERATURE_MIN)
    val temperatureMin: Int?,
    @SerializedName("iconDay")
    @ColumnInfo(name = Constants.CONDITION_CODE_DAY)
    val conditionCodeDay: Int?,
    @SerializedName("textDay")
    @ColumnInfo(name = Constants.CONDITION_NAME_DAY)
    val conditionNameDay: String?,
    @SerializedName("iconNight")
    @ColumnInfo(name = Constants.CONDITION_CODE_NIGHT)
    val conditionCodeNight: Int?,
    @SerializedName("textNight")
    @ColumnInfo(name = Constants.CONDITION_NAME_NIGHT)
    val conditionNameNight: String?,
    @SerializedName("wind360Day")
    @ColumnInfo(name = Constants.WIND_ANGLE_DAY)
    val windAngleDay: Int?,
    @SerializedName("windDirDay")
    @ColumnInfo(name = Constants.WIND_DIRECTION_DAY)
    val windDirectionDay: String?,
    @SerializedName("windScaleDay")
    @ColumnInfo(name = Constants.WIND_SCALE_DAY)
    val windScaleDay: IntRange?,
    @SerializedName("windSpeedDay")
    @ColumnInfo(name = Constants.WIND_SPEED_DAY)
    val windSpeedDay: Int?,
    @SerializedName("wind360Night")
    @ColumnInfo(name = Constants.WIND_ANGLE_NIGHT)
    val windAngleNight: Int?,
    @SerializedName("windDirNight")
    @ColumnInfo(name = Constants.WIND_DIRECTION_NIGHT)
    val windDirectionNight: String?,
    @SerializedName("windScaleNight")
    @ColumnInfo(name = Constants.WIND_SCALE_NIGHT)
    val windScaleNight: IntRange?,
    @SerializedName("windSpeedNight")
    @ColumnInfo(name = Constants.WIND_SPEED_NIGHT)
    val windSpeedNight: Int?,
    @SerializedName("humidity")
    @ColumnInfo(name = Constants.HUMIDITY)
    val humidity: Int?,
    @SerializedName("precip")
    @ColumnInfo(name = Constants.PRECIPITATION)
    val precipitation: Float?,
    @SerializedName("pressure")
    @ColumnInfo(name = Constants.PRESSURE)
    val pressure: Int?,
    @SerializedName("vis")
    @ColumnInfo(name = Constants.VISIBILITY)
    val visibility: Int?,
    @SerializedName("cloud")
    @ColumnInfo(name = Constants.CLOUD)
    val cloud: Int?,
    @SerializedName("uvIndex")
    @ColumnInfo(name = Constants.UV_INDEX)
    val uvIndex: Int?,
)