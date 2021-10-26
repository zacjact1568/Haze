package net.zackzhang.code.haze.weather.model.remote.converter

import net.zackzhang.code.haze.common.util.fromJsonObject
import net.zackzhang.code.haze.common.util.getUpdateTime
import net.zackzhang.code.haze.common.util.responseBodyToJsonObject
import net.zackzhang.code.haze.weather.model.entity.WeatherHourlyEntity
import okhttp3.ResponseBody
import retrofit2.Converter

class WeatherNowConverter : Converter<ResponseBody, WeatherHourlyEntity> {

    override fun convert(value: ResponseBody): WeatherHourlyEntity {
        val response = responseBodyToJsonObject(value)
        return fromJsonObject(response.getJSONObject("now"), WeatherHourlyEntity::class).apply {
            updatedAt = getUpdateTime(response)
        }
    }
}