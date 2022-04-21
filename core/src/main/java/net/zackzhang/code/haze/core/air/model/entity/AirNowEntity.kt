package net.zackzhang.code.haze.core.air.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.google.gson.annotations.SerializedName
import net.zackzhang.code.haze.core.city.model.entity.CityEntity
import net.zackzhang.code.haze.core.common.constant.*
import java.time.ZonedDateTime

@Entity(tableName = AIR, foreignKeys = [ForeignKey(
    entity = CityEntity::class,
    parentColumns = [ID],
    childColumns = [CITY_ID],
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE,
)], primaryKeys = [CITY_ID, STATION_ID])
data class AirNowEntity(
    @ColumnInfo(name = CITY_ID)
    var cityId: String,
    @ColumnInfo(name = UPDATED_AT)
    var updatedAt: ZonedDateTime?,
    @SerializedName("pubTime")
    @ColumnInfo(name = PUBLISHED_AT)
    val publishedAt: ZonedDateTime?,
    @SerializedName("name")
    @ColumnInfo(name = STATION_NAME)
    val stationName: String?,
    @SerializedName("id")
    @ColumnInfo(name = STATION_ID)
    var stationId: String,
    @SerializedName("aqi")
    @ColumnInfo(name = INDEX)
    val index: Int?,
    @SerializedName("level")
    @ColumnInfo(name = LEVEL)
    val level: Int?,
    @SerializedName("category")
    @ColumnInfo(name = CATEGORY)
    val category: String?,
    @SerializedName("primary")
    @ColumnInfo(name = PRIMARY)
    val primary: String?,
    @SerializedName("pm10")
    @ColumnInfo(name = PM_10)
    val pm10: Float?,
    @SerializedName("pm2p5")
    @ColumnInfo(name = PM_2_5)
    val pm2p5: Float?,
    @SerializedName("no2")
    @ColumnInfo(name = NO_2)
    val no2: Float?,
    @SerializedName("so2")
    @ColumnInfo(name = SO_2)
    val so2: Float?,
    @SerializedName("co")
    @ColumnInfo(name = CO)
    val co: Float?,
    @SerializedName("o3")
    @ColumnInfo(name = O_3)
    val o3: Float?,
) {

    val isNow get() = stationId.isEmpty()
}