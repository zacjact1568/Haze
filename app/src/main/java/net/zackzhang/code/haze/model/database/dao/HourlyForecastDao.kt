package net.zackzhang.code.haze.model.database.dao

import androidx.room.*
import io.reactivex.Single
import net.zackzhang.code.haze.common.Constant
import net.zackzhang.code.haze.model.database.entity.HourlyForecastEntity

@Dao
interface HourlyForecastDao {

    @Insert
    fun insert(dailyForecastEntities: Array<HourlyForecastEntity>)

    @Update
    fun update(dailyForecastEntities: Array<HourlyForecastEntity>)

    @Query("DELETE FROM ${Constant.HOURLY_FORECAST} WHERE ${Constant.CITY_ID} = :cityId")
    fun delete(cityId: String)

    @Query("SELECT * FROM ${Constant.HOURLY_FORECAST} ORDER BY ${Constant.ADD_TIME}, ${Constant.TIME}")
    fun loadAll(): Single<Array<HourlyForecastEntity>>
}