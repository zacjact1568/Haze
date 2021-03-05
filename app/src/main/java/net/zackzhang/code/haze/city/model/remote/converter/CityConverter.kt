package net.zackzhang.code.haze.city.model.remote.converter

import net.zackzhang.code.haze.city.model.entity.CityEntity
import net.zackzhang.code.haze.common.util.NetworkUtils
import okhttp3.ResponseBody
import retrofit2.Converter

class CityConverter : Converter<ResponseBody, List<CityEntity>> {

    override fun convert(value: ResponseBody): List<CityEntity> {
        val response = NetworkUtils.responseBodyToJsonObject(value)
        return NetworkUtils.fromJsonArray(response.getJSONArray("location"), CityEntity::class)
    }
}