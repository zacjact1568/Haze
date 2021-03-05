package net.zackzhang.code.haze.common.model.network.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import net.zackzhang.code.haze.common.util.DateTimeUtils
import java.time.ZonedDateTime

class DateTimeAdapter : TypeAdapter<ZonedDateTime?>() {

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
        return DateTimeUtils.parseDateTime(`in`.nextString())
    }
}