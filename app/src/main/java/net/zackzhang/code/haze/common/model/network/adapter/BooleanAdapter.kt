package net.zackzhang.code.haze.common.model.network.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import net.zackzhang.code.haze.common.util.DateTimeUtils

class BooleanAdapter : TypeAdapter<Boolean?>() {

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