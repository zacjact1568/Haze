package net.zackzhang.code.haze.common.model.network.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import net.zackzhang.code.haze.common.util.DateTimeUtils
import java.time.LocalTime

class TimeAdapter : TypeAdapter<LocalTime?>() {

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
        return DateTimeUtils.parseTime(`in`.nextString())
    }
}