package net.zackzhang.code.haze.weather.model.remote.converter

import net.zackzhang.code.haze.common.util.NetworkUtils
import net.zackzhang.code.haze.weather.model.entity.WeatherDailyEntity
import okhttp3.ResponseBody
import retrofit2.Converter

class WeatherDailyConverter : Converter<ResponseBody, List<WeatherDailyEntity>> {

    override fun convert(value: ResponseBody): List<WeatherDailyEntity> {
        val response = NetworkUtils.responseBodyToJsonObject(value)
        val updateTime = NetworkUtils.getUpdateTime(response)
        val dailyList = NetworkUtils.fromJsonArray(response.getJSONArray("daily"), WeatherDailyEntity::class)
        dailyList.forEach { it.updatedAt = updateTime }
        return dailyList
    }
}