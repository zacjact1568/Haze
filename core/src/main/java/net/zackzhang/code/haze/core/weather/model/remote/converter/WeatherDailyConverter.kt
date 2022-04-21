package net.zackzhang.code.haze.core.weather.model.remote.converter

import net.zackzhang.code.haze.base.util.fromJsonArray
import net.zackzhang.code.haze.base.util.getUpdateTime
import net.zackzhang.code.haze.base.util.responseBodyToJsonObject
import net.zackzhang.code.haze.core.weather.model.entity.WeatherDailyEntity
import okhttp3.ResponseBody
import retrofit2.Converter

class WeatherDailyConverter : Converter<ResponseBody, List<WeatherDailyEntity>> {

    override fun convert(value: ResponseBody): List<WeatherDailyEntity> {
        val response = responseBodyToJsonObject(value)
        val updateTime = getUpdateTime(response)
        val dailyList = fromJsonArray(response.getJSONArray("daily"), WeatherDailyEntity::class)
        dailyList.forEach { it.updatedAt = updateTime }
        return dailyList
    }
}