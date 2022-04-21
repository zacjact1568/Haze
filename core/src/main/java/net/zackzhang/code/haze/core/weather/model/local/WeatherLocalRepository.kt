package net.zackzhang.code.haze.core.weather.model.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.zackzhang.code.haze.core.weather.model.entity.WeatherEntity
import net.zackzhang.code.haze.core.common.model.local.AppDatabase.Companion.instance as db

object WeatherLocalRepository {

    suspend fun insert(weather: WeatherEntity, replace: Boolean = false) {
        withContext(Dispatchers.IO) {
            if (replace) {
                db.weatherDao().replace(weather.cityId, weather.dbHourly, weather.dbDaily, weather.dbAir)
            } else {
                db.weatherDao().insert(weather.dbHourly, weather.dbDaily, weather.dbAir)
            }
        }
    }

    suspend fun query() = withContext(Dispatchers.IO) {
        db.weatherDao().query()
    }
}