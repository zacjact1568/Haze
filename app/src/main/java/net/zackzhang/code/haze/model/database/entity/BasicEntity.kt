package net.zackzhang.code.haze.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.zackzhang.code.haze.common.Constant

@Entity(tableName = Constant.BASIC)
data class BasicEntity(
        @PrimaryKey
        @ColumnInfo(name = Constant.CITY_ID)
        val cityId: String,
        @ColumnInfo(name = Constant.CITY_NAME)
        val cityName: String,
        @ColumnInfo(name = Constant.IS_PREFECTURE)
        val isPrefecture: Boolean,
        @ColumnInfo(name = Constant.UPDATE_TIME)
        val updateTime: Long,
        @ColumnInfo(name = Constant.ADD_TIME)
        val addTime: Long
)