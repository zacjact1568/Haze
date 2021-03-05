package net.zackzhang.code.haze.weather.model.remote.converter

import net.zackzhang.code.haze.common.util.NetworkUtils
import net.zackzhang.code.haze.weather.model.entity.WeatherHourlyEntity
import okhttp3.ResponseBody
import retrofit2.Converter

class WeatherNowConverter : Converter<ResponseBody, WeatherHourlyEntity> {

    override fun convert(value: ResponseBody): WeatherHourlyEntity {
        val response = NetworkUtils.responseBodyToJsonObject(value)
        return NetworkUtils.fromJsonObject(response.getJSONObject("now"), WeatherHourlyEntity::class).apply {
            updatedAt = NetworkUtils.getUpdateTime(response)
        }
    }
}