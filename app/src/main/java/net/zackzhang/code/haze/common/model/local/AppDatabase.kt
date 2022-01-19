package net.zackzhang.code.haze.common.model.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.zackzhang.code.haze.App
import net.zackzhang.code.haze.air.model.entity.AirNowEntity
import net.zackzhang.code.haze.city.model.entity.CityEntity
import net.zackzhang.code.haze.city.model.local.CityDao
import net.zackzhang.code.haze.common.util.appName
import net.zackzhang.code.haze.weather.model.entity.WeatherDailyEntity
import net.zackzhang.code.haze.weather.model.entity.WeatherHourlyEntity
import net.zackzhang.code.haze.weather.model.local.WeatherDao

@Database(
    entities = [CityEntity::class, WeatherHourlyEntity::class, WeatherDailyEntity::class, AirNowEntity::class],
    version = AppDatabase.VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        const val VERSION = 1

        val instance = Room.databaseBuilder(App.context, AppDatabase::class.java, "$appName.db").build()
    }

    abstract fun cityDao(): CityDao

    abstract fun weatherDao(): WeatherDao
}