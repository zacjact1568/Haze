package net.zackzhang.code.haze.core.air.model.remote

import net.zackzhang.code.haze.core.air.model.entity.AirEntity
import net.zackzhang.code.haze.core.air.model.remote.converter.AirConverter
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class AirConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return when (type) {
            AirEntity::class.java -> AirConverter()
            else -> null
        }
    }
}