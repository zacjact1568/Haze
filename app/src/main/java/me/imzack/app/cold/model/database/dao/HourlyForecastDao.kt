package me.imzack.app.cold.model.database.dao

import android.arch.persistence.room.*
import io.reactivex.Single
import me.imzack.app.cold.common.Constant
import me.imzack.app.cold.model.database.entity.HourlyForecastEntity

@Dao
interface HourlyForecastDao {

    @Insert
    fun insert(dailyForecastEntities: Array<HourlyForecastEntity>)

    @Update
    fun update(dailyForecastEntities: Array<HourlyForecastEntity>)

    @Query("DELETE FROM ${Constant.HOURLY_FORECAST} WHERE ${Constant.CITY_ID} = :cityId")
    fun delete(cityId: String)

    @Query("SELECT * FROM ${Constant.HOURLY_FORECAST}")
    fun loadAll(): Single<Array<HourlyForecastEntity>>
}