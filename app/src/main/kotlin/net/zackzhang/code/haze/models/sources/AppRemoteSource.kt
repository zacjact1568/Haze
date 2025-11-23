package net.zackzhang.code.haze.models.sources

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import net.zackzhang.code.haze.utils.parseDate
import net.zackzhang.code.haze.utils.parseDateTime
import net.zackzhang.code.haze.utils.parseIntRange
import net.zackzhang.code.haze.utils.parseTime
import net.zackzhang.code.haze.utils.parseTimeZone
import net.zackzhang.code.haze.utils.parseUtcOffset
import net.zackzhang.code.haze.utils.presentIntRange
import net.zackzhang.code.util.log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

val AppGson by lazy {
    GsonBuilder()
        .registerTypeAdapterFactory(AppTypeAdapterFactory())
        .create()!!
}

private class AppTypeAdapterFactory : TypeAdapterFactory {

    @Suppress("UNCHECKED_CAST")
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

private class BooleanAdapter : TypeAdapter<Boolean?>() {

    override fun write(out: JsonWriter, value: Boolean?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.jsonValue(if (value) "1" else "0")
        }
    }

    override fun read(`in`: JsonReader): Boolean? {
        if (`in`.peek() != JsonToken.STRING) {
            `in`.skipValue()
            return null
        }
        return when (`in`.nextString()) {
            "0" -> false
            "1" -> true
            else -> null
        }
    }
}

private class IntRangeAdapter : TypeAdapter<IntRange?>() {

    override fun write(out: JsonWriter, value: IntRange?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.jsonValue(presentIntRange(value))
        }
    }

    override fun read(`in`: JsonReader): IntRange? {
        if (`in`.peek() != JsonToken.STRING) {
            `in`.skipValue()
            return null
        }
        return parseIntRange(`in`.nextString())
    }
}

private class DateAdapter : TypeAdapter<LocalDate?>() {

    override fun write(out: JsonWriter, value: LocalDate?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.jsonValue(value.toString())
        }
    }

    override fun read(`in`: JsonReader): LocalDate? {
        if (`in`.peek() != JsonToken.STRING) {
            `in`.skipValue()
            return null
        }
        return parseDate(`in`.nextString())
    }
}

private class TimeAdapter : TypeAdapter<LocalTime?>() {

    override fun write(out: JsonWriter, value: LocalTime?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.jsonValue(value.toString())
        }
    }

    override fun read(`in`: JsonReader): LocalTime? {
        if (`in`.peek() != JsonToken.STRING) {
            `in`.skipValue()
            return null
        }
        return parseTime(`in`.nextString())
    }
}

private class DateTimeAdapter : TypeAdapter<ZonedDateTime?>() {

    override fun write(out: JsonWriter, value: ZonedDateTime?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.jsonValue(value.toString())
        }
    }

    override fun read(`in`: JsonReader): ZonedDateTime? {
        if (`in`.peek() != JsonToken.STRING) {
            `in`.skipValue()
            return null
        }
        return parseDateTime(`in`.nextString())
    }
}

private class TimeZoneAdapter : TypeAdapter<ZoneId?>() {

    override fun write(out: JsonWriter, value: ZoneId?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.jsonValue(value.toString())
        }
    }

    override fun read(`in`: JsonReader): ZoneId? {
        if (`in`.peek() != JsonToken.STRING) {
            `in`.skipValue()
            return null
        }
        return parseTimeZone(`in`.nextString())
    }
}

private class UtcOffsetAdapter : TypeAdapter<ZoneOffset?>() {

    override fun write(out: JsonWriter, value: ZoneOffset?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.jsonValue(value.toString())
        }
    }

    override fun read(`in`: JsonReader): ZoneOffset? {
        if (`in`.peek() != JsonToken.STRING) {
            `in`.skipValue()
            return null
        }
        return parseUtcOffset(`in`.nextString())
    }
}

fun getOkHttpClient(tag: String) = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor { log(tag, it) }
        .setLevel(HttpLoggingInterceptor.Level.BODY))
    .addNetworkInterceptor(StethoInterceptor())
    .build()