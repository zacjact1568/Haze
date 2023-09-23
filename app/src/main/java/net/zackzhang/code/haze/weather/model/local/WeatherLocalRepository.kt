package net.zackzhang.code.haze.weather.model.local

import net.zackzhang.code.haze.weather.model.entity.WeatherEntity
import net.zackzhang.code.haze.common.model.local.AppDatabase.Companion.instance as db

object WeatherLocalRepository {

    suspend fun insert(weather: WeatherEntity, replace: Boolean = false) {
        // Room 的协程实现调用了线程池，无需使用 Dispatchers.IO
        // See DefaultTaskExecutor#mDiskIO
        if (replace) {
            db.weatherDao().replace(weather.cityId, weather.dbHourly, weather.dbDaily, weather.dbAir)
        } else {
            db.weatherDao().insert(weather.dbHourly, weather.dbDaily, weather.dbAir)
        }
    }

    suspend fun query() = db.weatherDao().query()
}