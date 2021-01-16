package net.zackzhang.code.haze.model.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import net.zackzhang.code.haze.common.Constant

@Entity(tableName = Constant.CURRENT)
data class CurrentEntity(
        @PrimaryKey
        @ColumnInfo(name = Constant.CITY_ID)
        val cityId: String,
        @ColumnInfo(name = Constant.CONDITION_CODE)
        val conditionCode: Int,
        @ColumnInfo(name = Constant.TEMPERATURE)
        val temperature: Int,
        @ColumnInfo(name = Constant.FEELS_LIKE)
        val feelsLike: Int,
        @ColumnInfo(name = Constant.AIR_QUALITY_INDEX)
        val airQualityIndex: Int,
        @ColumnInfo(name = Constant.ADD_TIME)
        val addTime: Long
)