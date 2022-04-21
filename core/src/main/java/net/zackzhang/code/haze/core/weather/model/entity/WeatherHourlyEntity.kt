package net.zackzhang.code.haze.core.weather.model.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import net.zackzhang.code.haze.core.city.model.entity.CityEntity
import net.zackzhang.code.haze.core.common.constant.*
import java.time.ZonedDateTime

@Entity(tableName = WEATHER_HOURLY, foreignKeys = [ForeignKey(
    entity = CityEntity::class,
    parentColumns = [ID],
    childColumns = [CITY_ID],
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE,
)], primaryKeys = [CITY_ID, TIME])
data class WeatherHourlyEntity(
    @ColumnInfo(name = CITY_ID)
    var cityId: String,
    @ColumnInfo(name = UPDATED_AT)
    var updatedAt: ZonedDateTime?,
    @SerializedName("fxTime", alternate = ["obsTime"])
    @ColumnInfo(name = TIME)
    val time: ZonedDateTime,
    @SerializedName("temp")
    @ColumnInfo(name = TEMPERATURE)
    val temperature: Int?,
    @SerializedName("feelsLike")
    @ColumnInfo(name = FEELS_LIKE)
    val feelsLike: Int?,
    @SerializedName("icon")
    @ColumnInfo(name = CONDITION_CODE)
    val conditionCode: Int?,
    @SerializedName("text")
    @ColumnInfo(name = CONDITION_NAME)
    val conditionName: String?,
    @SerializedName("wind360")
    @ColumnInfo(name = WIND_ANGLE)
    val windAngle: Int?,
    @SerializedName("windDir")
    @ColumnInfo(name = WIND_DIRECTION)
    val windDirection: String?,
    @SerializedName("windScale")
    @ColumnInfo(name = WIND_SCALE)
    val windScale: IntRange?,
    @SerializedName("windSpeed")
    @ColumnInfo(name = WIND_SPEED)
    val windSpeed: Int?,
    @SerializedName("humidity")
    @ColumnInfo(name = HUMIDITY)
    val humidity: Int?,
    @SerializedName("pop")
    @ColumnInfo(name = PROBABILITY)
    val probability: Int?,
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
    @SerializedName("dew")
    @ColumnInfo(name = DEW_POINT)
    val dewPoint: Int?,
) {

    val isNow get() = feelsLike != null && probability == null && visibility != null
}