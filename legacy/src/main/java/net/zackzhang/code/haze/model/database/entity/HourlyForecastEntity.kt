package net.zackzhang.code.haze.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import net.zackzhang.code.haze.common.Constant

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
        val precipitationProbability: Int,
        @ColumnInfo(name = Constant.ADD_TIME)
        val addTime: Long
)