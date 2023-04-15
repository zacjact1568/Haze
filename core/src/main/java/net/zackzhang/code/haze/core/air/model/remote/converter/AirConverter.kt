package net.zackzhang.code.haze.core.air.model.remote.converter

import net.zackzhang.code.haze.core.air.model.entity.AirEntity
import net.zackzhang.code.haze.core.air.model.entity.AirNowEntity
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
        val rawStationList = response.getList("station", AirNowEntity::class)
        val stationList = rawStationList?.run {
            val map = linkedMapOf<String, AirNowEntity>()
            forEach {
                it.updatedAt = updateTime
                // 服务端可能返回多个相同的 stationId，去重
                map[it.stationId] = it
            }
            ArrayList(map.values)
        }
        return AirEntity(now, stationList)
    }
}