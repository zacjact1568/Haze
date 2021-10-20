package net.zackzhang.code.haze.air.model.entity

data class AirEntity(val now: AirNowEntity, val stations: List<AirNowEntity>?) {

    companion object {

        fun fromAirNowEntityList(list: List<AirNowEntity>) = AirEntity(
            list.first { it.isNow },
            list.filterNot { it.isNow }
        )
    }

    fun attachCityId(cityId: String) {
        now.cityId = cityId
        now.stationId = ""
        stations?.forEach { it.cityId = cityId }
    }
}
