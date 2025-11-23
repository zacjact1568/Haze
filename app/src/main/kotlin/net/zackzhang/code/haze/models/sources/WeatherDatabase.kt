package net.zackzhang.code.haze.models.sources

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime

@Dao
interface WeatherDao {

    @Insert
    suspend fun insert(
        hourlyList: List<WeatherHourlyEntity>,
        dailyList: List<WeatherDailyEntity>,
        airList: List<AirNowEntity>,
    )

    @Query("DELETE FROM weather_hourly WHERE city_id = :cityId")
    suspend fun deleteHourly(cityId: String)

    @Query("DELETE FROM weather_daily WHERE city_id = :cityId")
    suspend fun deleteDaily(cityId: String)

    @Query("DELETE FROM air WHERE city_id = :cityId")
    suspend fun deleteAir(cityId: String)

    @Transaction
    suspend fun replace(
        cityId: String,
        hourlyList: List<WeatherHourlyEntity>,
        dailyList: List<WeatherDailyEntity>,
        airList: List<AirNowEntity>,
    ) {
        deleteHourly(cityId)
        deleteDaily(cityId)
        deleteAir(cityId)
        insert(hourlyList, dailyList, airList)
    }

    @Transaction
    @Query("SELECT id, name FROM city ORDER BY created_at")
    suspend fun query(): List<WeatherLocalEntity>
}

@Entity(
    tableName = "weather_hourly",
    foreignKeys = [
        ForeignKey(
            entity = CityEntity::class,
            parentColumns = ["id"],
            childColumns = ["city_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
    primaryKeys = ["city_id", "time"],
)
data class WeatherHourlyEntity(
    @ColumnInfo(name = "city_id")
    var cityId: String,
    @ColumnInfo(name = "updated_at")
    var updatedAt: ZonedDateTime?,
    @SerializedName("fxTime", alternate = ["obsTime"])
    @ColumnInfo(name = "time")
    val time: ZonedDateTime,
    @SerializedName("temp")
    @ColumnInfo(name = "temperature")
    val temperature: Int?,
    @SerializedName("feelsLike")
    @ColumnInfo(name = "feels_like")
    val feelsLike: Int?,
    @SerializedName("icon")
    @ColumnInfo(name = "condition_code")
    val conditionCode: Int?,
    @SerializedName("text")
    @ColumnInfo(name = "condition_name")
    val conditionName: String?,
    @SerializedName("wind360")
    @ColumnInfo(name = "wind_angle")
    val windAngle: Int?,
    @SerializedName("windDir")
    @ColumnInfo(name = "wind_direction")
    val windDirection: String?,
    @SerializedName("windScale")
    @ColumnInfo(name = "wind_scale")
    val windScale: IntRange?,
    @SerializedName("windSpeed")
    @ColumnInfo(name = "wind_speed")
    val windSpeed: Int?,
    @SerializedName("humidity")
    @ColumnInfo(name = "humidity")
    val humidity: Int?,
    @SerializedName("pop")
    @ColumnInfo(name = "probability")
    val probability: Int?,
    @SerializedName("precip")
    @ColumnInfo(name = "precipitation")
    val precipitation: Float?,
    @SerializedName("pressure")
    @ColumnInfo(name = "pressure")
    val pressure: Int?,
    @SerializedName("vis")
    @ColumnInfo(name = "visibility")
    val visibility: Int?,
    @SerializedName("cloud")
    @ColumnInfo(name = "cloud")
    val cloud: Int?,
    @SerializedName("dew")
    @ColumnInfo(name = "dew_point")
    val dewPoint: Int?,
) {

    val isNow get() = feelsLike != null && probability == null && visibility != null
}

@Entity(
    tableName = "weather_daily",
    foreignKeys = [
        ForeignKey(
            entity = CityEntity::class,
            parentColumns = ["id"],
            childColumns = ["city_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
    primaryKeys = ["city_id", "date"],
)
data class WeatherDailyEntity(
    @ColumnInfo(name = "city_id")
    var cityId: String,
    @ColumnInfo(name = "updated_at")
    var updatedAt: ZonedDateTime?,
    @SerializedName("fxDate")
    @ColumnInfo(name = "date")
    val date: LocalDate,
    @SerializedName("sunrise")
    @ColumnInfo(name = "sunrise")
    val sunrise: LocalTime?,
    @SerializedName("sunset")
    @ColumnInfo(name = "sunset")
    val sunset: LocalTime?,
    @SerializedName("moonrise")
    @ColumnInfo(name = "moonrise")
    val moonrise: LocalTime?,
    @SerializedName("moonset")
    @ColumnInfo(name = "moonset")
    val moonset: LocalTime?,
    @SerializedName("moonPhase")
    @ColumnInfo(name = "moon_phase")
    val moonPhase: String?,
    @SerializedName("tempMax")
    @ColumnInfo(name = "temperature_max")
    val temperatureMax: Int?,
    @SerializedName("tempMin")
    @ColumnInfo(name = "temperature_min")
    val temperatureMin: Int?,
    @SerializedName("iconDay")
    @ColumnInfo(name = "condition_code_day")
    val conditionCodeDay: Int?,
    @SerializedName("textDay")
    @ColumnInfo(name = "condition_name_day")
    val conditionNameDay: String?,
    @SerializedName("iconNight")
    @ColumnInfo(name = "condition_code_night")
    val conditionCodeNight: Int?,
    @SerializedName("textNight")
    @ColumnInfo(name = "condition_name_night")
    val conditionNameNight: String?,
    @SerializedName("wind360Day")
    @ColumnInfo(name = "wind_angle_day")
    val windAngleDay: Int?,
    @SerializedName("windDirDay")
    @ColumnInfo(name = "wind_direction_day")
    val windDirectionDay: String?,
    @SerializedName("windScaleDay")
    @ColumnInfo(name = "wind_scale_day")
    val windScaleDay: IntRange?,
    @SerializedName("windSpeedDay")
    @ColumnInfo(name = "wind_speed_day")
    val windSpeedDay: Int?,
    @SerializedName("wind360Night")
    @ColumnInfo(name = "wind_angle_night")
    val windAngleNight: Int?,
    @SerializedName("windDirNight")
    @ColumnInfo(name = "wind_direction_night")
    val windDirectionNight: String?,
    @SerializedName("windScaleNight")
    @ColumnInfo(name = "wind_scale_night")
    val windScaleNight: IntRange?,
    @SerializedName("windSpeedNight")
    @ColumnInfo(name = "wind_speed_night")
    val windSpeedNight: Int?,
    @SerializedName("humidity")
    @ColumnInfo(name = "humidity")
    val humidity: Int?,
    @SerializedName("precip")
    @ColumnInfo(name = "precipitation")
    val precipitation: Float?,
    @SerializedName("pressure")
    @ColumnInfo(name = "pressure")
    val pressure: Int?,
    @SerializedName("vis")
    @ColumnInfo(name = "visibility")
    val visibility: Int?,
    @SerializedName("cloud")
    @ColumnInfo(name = "cloud")
    val cloud: Int?,
    @SerializedName("uvIndex")
    @ColumnInfo(name = "uv_index")
    val uvIndex: Int?,
) {

    val temperatureRange
        get() = if (temperatureMin == null || temperatureMax == null) null else {
            temperatureMin..temperatureMax
        }
}

@Entity(
    tableName = "air",
    foreignKeys = [
        ForeignKey(
            entity = CityEntity::class,
            parentColumns = ["id"],
            childColumns = ["city_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
    primaryKeys = ["city_id", "station_id"],
)
data class AirNowEntity(
    @ColumnInfo(name = "city_id")
    var cityId: String,
    @ColumnInfo(name = "updated_at")
    var updatedAt: ZonedDateTime?,
    @SerializedName("pubTime")
    @ColumnInfo(name = "published_at")
    val publishedAt: ZonedDateTime?,
    @SerializedName("name")
    @ColumnInfo(name = "station_name")
    val stationName: String?,
    @SerializedName("id")
    @ColumnInfo(name = "station_id")
    var stationId: String,
    @SerializedName("aqi")
    @ColumnInfo(name = "index")
    val index: Int?,
    @SerializedName("level")
    @ColumnInfo(name = "level")
    val level: Int?,
    @SerializedName("category")
    @ColumnInfo(name = "category")
    val category: String?,
    @SerializedName("primary")
    @ColumnInfo(name = "primary")
    val primary: String?,
    @SerializedName("pm10")
    @ColumnInfo(name = "pm_10")
    val pm10: Float?,
    @SerializedName("pm2p5")
    @ColumnInfo(name = "pm_2_5")
    val pm2p5: Float?,
    @SerializedName("no2")
    @ColumnInfo(name = "no_2")
    val no2: Float?,
    @SerializedName("so2")
    @ColumnInfo(name = "so_2")
    val so2: Float?,
    @SerializedName("co")
    @ColumnInfo(name = "co")
    val co: Float?,
    @SerializedName("o3")
    @ColumnInfo(name = "o_3")
    val o3: Float?,
) {

    val isNow get() = stationId.isEmpty()
}

@Parcelize
data class CityWeatherEntity(
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String?,
) : Parcelable

data class WeatherLocalEntity(
    @Embedded
    val city: CityWeatherEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "city_id",
    )
    val hourlyList: List<WeatherHourlyEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "city_id",
    )
    val dailyList: List<WeatherDailyEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "city_id",
    )
    val airList: List<AirNowEntity>,
)