package net.zackzhang.code.haze.city.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import net.zackzhang.code.haze.common.constant.*
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Entity(tableName = CITY)
data class CityEntity(
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: String,
    @SerializedName("name")
    @ColumnInfo(name = NAME)
    val name: String?,
    @SerializedName("lat")
    @ColumnInfo(name = LATITUDE)
    val latitude: Float?,
    @SerializedName("lon")
    @ColumnInfo(name = LONGITUDE)
    val longitude: Float?,
    @SerializedName("adm2")
    @ColumnInfo(name = PREFECTURE)
    val prefecture: String?,
    @SerializedName("adm1")
    @ColumnInfo(name = PROVINCE)
    val province: String?,
    @SerializedName("country")
    @ColumnInfo(name = COUNTRY)
    val country: String?,
    @SerializedName("tz")
    @ColumnInfo(name = TIME_ZONE)
    val timeZone: ZoneId?,
    @SerializedName("utcOffset")
    @ColumnInfo(name = UTC_OFFSET)
    val utcOffset: ZoneOffset?,
    // Room 会自动以 INTEGER 储存 Boolean，不需再用 TypeConverter 转换
    @SerializedName("isDst")
    @ColumnInfo(name = DST)
    val dst: Boolean?,
    @SerializedName("type")
    @ColumnInfo(name = TYPE)
    val type: String?,
    @SerializedName("rank")
    @ColumnInfo(name = RANK)
    val rank: Int?,
    // 用于城市列表的排序
    @ColumnInfo(name = CREATED_AT)
    var createdAt: ZonedDateTime?,
)