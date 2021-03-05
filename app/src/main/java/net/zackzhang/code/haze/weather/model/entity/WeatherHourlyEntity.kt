package net.zackzhang.code.haze.weather.model.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import net.zackzhang.code.haze.city.model.entity.CityEntity
import net.zackzhang.code.haze.common.Constants
import java.time.ZonedDateTime

@Entity(tableName = Constants.WEATHER_HOURLY, foreignKeys = [ForeignKey(
    entity = CityEntity::class,
    parentColumns = [Constants.ID],
    childColumns = [Constants.CITY_ID],
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE,
)], primaryKeys = [Constants.CITY_ID, Constants.TIME])
data class WeatherHourlyEntity(
    @ColumnInfo(name = Constants.CITY_ID)
    var cityId: String,
    @ColumnInfo(name = Constants.UPDATED_AT)
    var updatedAt: ZonedDateTime?,
    @SerializedName("fxTime", alternate = ["obsTime"])
    @ColumnInfo(name = Constants.TIME)
    val time: ZonedDateTime,
    @SerializedName("temp")
    @ColumnInfo(name = Constants.TEMPERATURE)
    val temperature: Int?,
    @SerializedName("feelsLike")
    @ColumnInfo(name = Constants.FEELS_LIKE)
    val feelsLike: Int?,
    @SerializedName("icon")
    @ColumnInfo(name = Constants.CONDITION_CODE)
    val conditionCode: Int?,
    @SerializedName("text")
    @ColumnInfo(name = Constants.CONDITION_NAME)
    val conditionName: String?,
    @SerializedName("wind360")
    @ColumnInfo(name = Constants.WIND_ANGLE)
    val windAngle: Int?,
    @SerializedName("windDir")
    @ColumnInfo(name = Constants.WIND_DIRECTION)
    val windDirection: String?,
    @SerializedName("windScale")
    @ColumnInfo(name = Constants.WIND_SCALE)
    val windScale: IntRange?,
    @SerializedName("windSpeed")
    @ColumnInfo(name = Constants.WIND_SPEED)
    val windSpeed: Int?,
    @SerializedName("humidity")
    @ColumnInfo(name = Constants.HUMIDITY)
    val humidity: Int?,
    @SerializedName("pop")
    @ColumnInfo(name = Constants.PROBABILITY)
    val probability: Int?,
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
    @SerializedName("dew")
    @ColumnInfo(name = Constants.DEW_POINT)
    val dewPoint: Int?,
) {

    val isNow get() = feelsLike != null && probability == null && visibility != null
}