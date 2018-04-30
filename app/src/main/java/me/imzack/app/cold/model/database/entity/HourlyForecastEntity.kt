package me.imzack.app.cold.model.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import me.imzack.app.cold.common.Constant

@Entity(tableName = Constant.HOURLY_FORECAST, primaryKeys = [Constant.CITY_ID, Constant.SEQUENCE])
data class HourlyForecastEntity(
        @ColumnInfo(name = Constant.CITY_ID)
        val cityId: String,
        @ColumnInfo(name = Constant.SEQUENCE)
        val sequence: Int,
        @ColumnInfo(name = Constant.TIME)
        val time: Long,
        @ColumnInfo(name = Constant.TEMPERATURE)
        val temperature: Int,
        @ColumnInfo(name = Constant.PRECIPITATION_PROBABILITY)
        val precipitationProbability: Int
)