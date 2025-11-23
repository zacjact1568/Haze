package net.zackzhang.code.haze.models.sources

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import com.google.gson.annotations.SerializedName
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Dao
interface CityDao {

    @Insert
    suspend fun insert(entity: CityEntity)

    // 有外键约束，会同时删除 weather 和 air 表中对应城市的列
    @Query("DELETE FROM city WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM city")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceBy(entity: CityEntity) {
        deleteAll()
        insert(entity)
    }
}

@Entity(tableName = "city")
data class CityEntity(
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String?,
    @SerializedName("lat")
    @ColumnInfo(name = "latitude")
    val latitude: Float?,
    @SerializedName("lon")
    @ColumnInfo(name = "longitude")
    val longitude: Float?,
    @SerializedName("adm2")
    @ColumnInfo(name = "prefecture")
    val prefecture: String?,
    @SerializedName("adm1")
    @ColumnInfo(name = "province")
    val province: String?,
    @SerializedName("country")
    @ColumnInfo(name = "country")
    val country: String?,
    @SerializedName("tz")
    @ColumnInfo(name = "time_zone")
    val timeZone: ZoneId?,
    @SerializedName("utcOffset")
    @ColumnInfo(name = "utc_offset")
    val utcOffset: ZoneOffset?,
    // Room 会自动以 INTEGER 储存 Boolean，不需再用 TypeConverter 转换
    @SerializedName("isDst")
    @ColumnInfo(name = "dst")
    val dst: Boolean?,
    @SerializedName("type")
    @ColumnInfo(name = "type")
    val type: String?,
    @SerializedName("rank")
    @ColumnInfo(name = "rank")
    val rank: Int?,
    // 用于城市列表的排序
    @ColumnInfo(name = "created_at")
    var createdAt: ZonedDateTime?,
)