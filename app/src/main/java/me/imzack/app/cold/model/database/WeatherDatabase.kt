package me.imzack.app.cold.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import me.imzack.app.cold.model.database.dao.BasicDao
import me.imzack.app.cold.model.database.dao.CurrentDao
import me.imzack.app.cold.model.database.dao.DailyForecastDao
import me.imzack.app.cold.model.database.dao.HourlyForecastDao
import me.imzack.app.cold.model.database.entity.BasicEntity
import me.imzack.app.cold.model.database.entity.CurrentEntity
import me.imzack.app.cold.model.database.entity.DailyForecastEntity
import me.imzack.app.cold.model.database.entity.HourlyForecastEntity

@Database(entities = [BasicEntity::class, CurrentEntity::class, HourlyForecastEntity::class, DailyForecastEntity::class], version = WeatherDatabase.VERSION)
abstract class WeatherDatabase : RoomDatabase() {

    companion object {

        const val VERSION = 1

        const val NAME = "db_weather.db"
    }

    abstract fun basicDao(): BasicDao

    abstract fun currentDao(): CurrentDao

    abstract fun hourlyForecastDao(): HourlyForecastDao

    abstract fun dailyForecastDao(): DailyForecastDao
}