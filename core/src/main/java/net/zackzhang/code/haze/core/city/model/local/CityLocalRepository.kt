package net.zackzhang.code.haze.core.city.model.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.zackzhang.code.haze.core.city.model.entity.CityEntity
import java.time.ZonedDateTime
import net.zackzhang.code.haze.core.common.model.local.AppDatabase.Companion.instance as db

object CityLocalRepository {

    suspend fun replaceBy(city: CityEntity) {
        city.createdAt = ZonedDateTime.now()
        db.cityDao().replaceBy(city)
    }
}