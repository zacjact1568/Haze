package net.zackzhang.code.haze.air.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.google.gson.annotations.SerializedName
import net.zackzhang.code.haze.city.model.entity.CityEntity
import net.zackzhang.code.haze.common.Constants
import java.time.ZonedDateTime

@Entity(tableName = Constants.AIR, foreignKeys = [ForeignKey(
    entity = CityEntity::class,
    parentColumns = [Constants.ID],
    childColumns = [Constants.CITY_ID],
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE,
)], primaryKeys = [Constants.CITY_ID, Constants.STATION_ID])
data class AirNowEntity(
    @ColumnInfo(name = Constants.CITY_ID)
    var cityId: String,
    @ColumnInfo(name = Constants.UPDATED_AT)
    var updatedAt: ZonedDateTime?,
    @SerializedName("pubTime")
    @ColumnInfo(name = Constants.PUBLISHED_AT)
    val publishedAt: ZonedDateTime?,
    @SerializedName("name")
    @ColumnInfo(name = Constants.STATION_NAME)
    val stationName: String?,
    @SerializedName("id")
    @ColumnInfo(name = Constants.STATION_ID)
    var stationId: String,
    @SerializedName("aqi")
    @ColumnInfo(name = Constants.INDEX)
    val index: Int?,
    @SerializedName("level")
    @ColumnInfo(name = Constants.LEVEL)
    val level: Int?,
    @SerializedName("category")
    @ColumnInfo(name = Constants.CATEGORY)
    val category: String?,
    @SerializedName("primary")
    @ColumnInfo(name = Constants.PRIMARY)
    val primary: String?,
    @SerializedName("pm10")
    @ColumnInfo(name = Constants.PM_10)
    val pm10: Float?,
    @SerializedName("pm2p5")
    @ColumnInfo(name = Constants.PM_2_5)
    val pm2p5: Float?,
    @SerializedName("no2")
    @ColumnInfo(name = Constants.NO_2)
    val no2: Float?,
    @SerializedName("so2")
    @ColumnInfo(name = Constants.SO_2)
    val so2: Float?,
    @SerializedName("co")
    @ColumnInfo(name = Constants.CO)
    val co: Float?,
    @SerializedName("o3")
    @ColumnInfo(name = Constants.O_3)
    val o3: Float?,
) {

    val isNow get() = stationId.isEmpty()
}