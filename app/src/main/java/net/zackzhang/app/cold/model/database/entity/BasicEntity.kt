package net.zackzhang.app.cold.model.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import net.zackzhang.app.cold.common.Constant

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