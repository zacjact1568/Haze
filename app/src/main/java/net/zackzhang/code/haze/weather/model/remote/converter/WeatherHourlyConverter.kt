package net.zackzhang.code.haze.weather.model.remote.converter

import net.zackzhang.code.haze.common.util.fromJsonArray
import net.zackzhang.code.haze.common.util.getUpdateTime
import net.zackzhang.code.haze.common.util.responseBodyToJsonObject
import net.zackzhang.code.haze.weather.model.entity.WeatherHourlyEntity
import okhttp3.ResponseBody
import retrofit2.Converter

class WeatherHourlyConverter : Converter<ResponseBody, List<WeatherHourlyEntity>> {

    override fun convert(value: ResponseBody): List<WeatherHourlyEntity> {
        val response = responseBodyToJsonObject(value)
        val updateTime = getUpdateTime(response)
        val hourlyList = fromJsonArray(response.getJSONArray("hourly"), WeatherHourlyEntity::class)
        hourlyList.forEach { it.updatedAt = updateTime }
        return hourlyList
    }
}