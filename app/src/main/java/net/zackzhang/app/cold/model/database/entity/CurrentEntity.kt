package net.zackzhang.app.cold.model.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import net.zackzhang.app.cold.common.Constant

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
        val airQualityIndex: Int
)