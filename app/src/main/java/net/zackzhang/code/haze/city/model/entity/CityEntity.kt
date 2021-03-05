package net.zackzhang.code.haze.city.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import net.zackzhang.code.haze.common.Constants
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Entity(tableName = Constants.CITY)
data class CityEntity(
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = Constants.ID)
    val id: String,
    @SerializedName("name")
    @ColumnInfo(name = Constants.NAME)
    val name: String?,
    @SerializedName("lat")
    @ColumnInfo(name = Constants.LATITUDE)
    val latitude: Float?,
    @SerializedName("lon")
    @ColumnInfo(name = Constants.LONGITUDE)
    val longitude: Float?,
    @SerializedName("adm2")
    @ColumnInfo(name = Constants.PREFECTURE)
    val prefecture: String?,
    @SerializedName("adm1")
    @ColumnInfo(name = Constants.PROVINCE)
    val province: String?,
    @SerializedName("country")
    @ColumnInfo(name = Constants.COUNTRY)
    val country: String?,
    @SerializedName("tz")
    @ColumnInfo(name = Constants.TIME_ZONE)
    val timeZone: ZoneId?,
    @SerializedName("utcOffset")
    @ColumnInfo(name = Constants.UTC_OFFSET)
    val utcOffset: ZoneOffset?,
    // Room 会自动以 INTEGER 储存 Boolean，不需再用 TypeConverter 转换
    @SerializedName("isDst")
    @ColumnInfo(name = Constants.DST)
    val dst: Boolean?,
    @SerializedName("type")
    @ColumnInfo(name = Constants.TYPE)
    val type: String?,
    @SerializedName("rank")
    @ColumnInfo(name = Constants.RANK)
    val rank: Int?,
    // 用于城市列表的排序
    @ColumnInfo(name = Constants.CREATED_AT)
    var createdAt: ZonedDateTime?,
)