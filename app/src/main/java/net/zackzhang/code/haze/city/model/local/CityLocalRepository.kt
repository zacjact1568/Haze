package net.zackzhang.code.haze.city.model.local

import net.zackzhang.code.haze.city.model.entity.CityEntity
import java.time.ZonedDateTime
import net.zackzhang.code.haze.common.model.local.AppDatabase.Companion.instance as db

object CityLocalRepository {

    suspend fun replaceBy(city: CityEntity) {
        city.createdAt = ZonedDateTime.now()
        db.cityDao().replaceBy(city)
    }
}