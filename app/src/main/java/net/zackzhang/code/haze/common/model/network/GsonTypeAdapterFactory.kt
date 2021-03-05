package net.zackzhang.code.haze.common.model.network

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import net.zackzhang.code.haze.common.model.network.adapter.*
import java.time.*

class GsonTypeAdapterFactory : TypeAdapterFactory {

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        return when (type.rawType) {
            Boolean::class.java -> BooleanAdapter()
            IntRange::class.java -> IntRangeAdapter()
            LocalDate::class.java -> DateAdapter()
            LocalTime::class.java -> TimeAdapter()
            ZonedDateTime::class.java -> DateTimeAdapter()
            ZoneId::class.java -> TimeZoneAdapter()
            ZoneOffset::class.java -> UtcOffsetAdapter()
            else -> null
        } as? TypeAdapter<T>
    }
}