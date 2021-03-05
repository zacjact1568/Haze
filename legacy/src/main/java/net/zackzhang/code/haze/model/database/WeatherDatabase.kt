package net.zackzhang.code.haze.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import net.zackzhang.code.haze.model.database.dao.BasicDao
import net.zackzhang.code.haze.model.database.dao.CurrentDao
import net.zackzhang.code.haze.model.database.dao.DailyForecastDao
import net.zackzhang.code.haze.model.database.dao.HourlyForecastDao
import net.zackzhang.code.haze.model.database.entity.BasicEntity
import net.zackzhang.code.haze.model.database.entity.CurrentEntity
import net.zackzhang.code.haze.model.database.entity.DailyForecastEntity
import net.zackzhang.code.haze.model.database.entity.HourlyForecastEntity

@Database(entities = [BasicEntity::class, CurrentEntity::class, HourlyForecastEntity::class, DailyForecastEntity::class], version = WeatherDatabase.VERSION, exportSchema = false)
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