package net.zackzhang.code.haze.air.model.entity

data class AirEntity(val now: AirNowEntity, val stations: List<AirNowEntity>?) {

    companion object {

        fun fromAirNowEntityList(list: List<AirNowEntity>) =
            if (list.isNotEmpty()) AirEntity(
                list.first { it.isNow },
                list.filterNot { it.isNow }
            ) else null
    }

    fun attachCityId(cityId: String) {
        now.cityId = cityId
        now.stationId = ""
        stations?.forEach { it.cityId = cityId }
    }
}
