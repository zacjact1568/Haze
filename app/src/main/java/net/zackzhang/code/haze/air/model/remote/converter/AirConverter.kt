package net.zackzhang.code.haze.air.model.remote.converter

import net.zackzhang.code.haze.air.model.entity.AirEntity
import net.zackzhang.code.haze.air.model.entity.AirNowEntity
import net.zackzhang.code.haze.base.util.fromJsonObject
import net.zackzhang.code.haze.base.util.getList
import net.zackzhang.code.haze.base.util.getUpdateTime
import net.zackzhang.code.haze.base.util.responseBodyToJsonObject
import okhttp3.ResponseBody
import retrofit2.Converter

class AirConverter : Converter<ResponseBody, AirEntity> {

    override fun convert(value: ResponseBody): AirEntity {
        val response = responseBodyToJsonObject(value)
        val updateTime = getUpdateTime(response)
        val now = fromJsonObject(response.getJSONObject("now"), AirNowEntity::class).apply {
            updatedAt = updateTime
        }
        val stationList = response.getList("station", AirNowEntity::class)
        stationList?.forEach { it.updatedAt = updateTime }
        return AirEntity(now, stationList)
    }
}