package net.zackzhang.app.cold.model.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import net.zackzhang.app.cold.common.Constant

@Entity(tableName = Constant.DAILY_FORECAST, primaryKeys = [Constant.CITY_ID, Constant.SEQUENCE])
data class DailyForecastEntity(
        @ColumnInfo(name = Constant.CITY_ID)
        val cityId: String,
        @ColumnInfo(name = Constant.SEQUENCE)
        val sequence: Int,
        @ColumnInfo(name = Constant.DATE)
        val date: Long,
        @ColumnInfo(name = Constant.TEMPERATURE_MAX)
        val temperatureMax: Int,
        @ColumnInfo(name = Constant.TEMPERATURE_MIN)
        val temperatureMin: Int,
        @ColumnInfo(name = Constant.CONDITION_CODE_DAY)
        val conditionCodeDay: Int,
        @ColumnInfo(name = Constant.CONDITION_CODE_NIGHT)
        val conditionCodeNight: Int,
        @ColumnInfo(name = Constant.PRECIPITATION_PROBABILITY)
        val precipitationProbability: Int
)