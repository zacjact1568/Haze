package net.zackzhang.code.haze.model.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import net.zackzhang.code.haze.common.Constant

@Entity(tableName = Constant.DAILY_FORECAST, primaryKeys = [Constant.CITY_ID, Constant.SEQUENCE])
data class DailyForecastEntity(
        @ColumnInfo(name = Constant.CITY_ID)
        val cityId: String,
        // 不能用 date 作为第二主键
        // 因为如果添加城市后使用数组下标占位，稍后从网络获取到真正的 date，要更新此表的时候，在数据库中找不到对应的 date，就无法更新
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
        val precipitationProbability: Int,
        @ColumnInfo(name = Constant.ADD_TIME)
        val addTime: Long
)