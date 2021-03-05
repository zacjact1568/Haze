package net.zackzhang.code.haze.weather.model.remote.converter

import net.zackzhang.code.haze.common.util.NetworkUtils
import net.zackzhang.code.haze.weather.model.entity.WeatherHourlyEntity
import okhttp3.ResponseBody
import retrofit2.Converter

class WeatherHourlyConverter : Converter<ResponseBody, List<WeatherHourlyEntity>> {

    override fun convert(value: ResponseBody): List<WeatherHourlyEntity> {
        val response = NetworkUtils.responseBodyToJsonObject(value)
        val updateTime = NetworkUtils.getUpdateTime(response)
        val hourlyList = NetworkUtils.fromJsonArray(response.getJSONArray("hourly"), WeatherHourlyEntity::class)
        hourlyList.forEach { it.updatedAt = updateTime }
        return hourlyList
    }
}