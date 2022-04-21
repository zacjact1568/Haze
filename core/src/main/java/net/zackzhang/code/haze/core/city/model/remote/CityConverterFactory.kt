package net.zackzhang.code.haze.core.city.model.remote

import net.zackzhang.code.haze.core.city.model.entity.CityEntity
import net.zackzhang.code.haze.core.city.model.remote.converter.CityConverter
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class CityConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return when (type) {
            is ParameterizedType -> when (type.actualTypeArguments[0]) {
                CityEntity::class.java -> CityConverter()
                else -> null
            }
            else -> null
        }
    }
}