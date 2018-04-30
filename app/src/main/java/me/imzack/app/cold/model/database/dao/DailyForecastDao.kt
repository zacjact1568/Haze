package me.imzack.app.cold.model.database.dao

import android.arch.persistence.room.*
import io.reactivex.Single
import me.imzack.app.cold.common.Constant
import me.imzack.app.cold.model.database.entity.DailyForecastEntity

@Dao
interface DailyForecastDao {

    @Insert
    fun insert(dailyForecastEntities: Array<DailyForecastEntity>)

    @Update
    fun update(dailyForecastEntities: Array<DailyForecastEntity>)

    @Query("DELETE FROM ${Constant.DAILY_FORECAST} WHERE ${Constant.CITY_ID} = :cityId")
    fun delete(cityId: String)

    @Query("SELECT * FROM ${Constant.DAILY_FORECAST}")
    fun loadAll(): Single<Array<DailyForecastEntity>>
}