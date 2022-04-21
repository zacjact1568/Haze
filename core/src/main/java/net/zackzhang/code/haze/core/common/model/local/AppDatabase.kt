package net.zackzhang.code.haze.core.common.model.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.zackzhang.code.haze.core.air.model.entity.AirNowEntity
import net.zackzhang.code.haze.core.city.model.entity.CityEntity
import net.zackzhang.code.haze.core.city.model.local.CityDao
import net.zackzhang.code.haze.base.model.local.Converters
import net.zackzhang.code.haze.base.util.appName
import net.zackzhang.code.haze.base.util.context
import net.zackzhang.code.haze.core.weather.model.entity.WeatherDailyEntity
import net.zackzhang.code.haze.core.weather.model.entity.WeatherHourlyEntity
import net.zackzhang.code.haze.core.weather.model.local.WeatherDao

@Database(
    entities = [CityEntity::class, WeatherHourlyEntity::class, WeatherDailyEntity::class, AirNowEntity::class],
    version = AppDatabase.VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        const val VERSION = 1

        val instance = Room.databaseBuilder(context, AppDatabase::class.java, "$appName.db").build()
    }

    abstract fun cityDao(): CityDao

    abstract fun weatherDao(): WeatherDao
}