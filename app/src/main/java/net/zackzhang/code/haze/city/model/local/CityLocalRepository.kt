package net.zackzhang.code.haze.city.model.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.zackzhang.code.haze.city.model.entity.CityEntity
import java.time.ZonedDateTime
import net.zackzhang.code.haze.common.model.local.HazeDatabase.Companion.instance as db

object CityLocalRepository {

    suspend fun replaceBy(city: CityEntity) {
        city.createdAt = ZonedDateTime.now()
        withContext(Dispatchers.IO) {
            db.cityDao().replaceBy(city)
        }
    }
}