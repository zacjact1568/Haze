package net.zackzhang.code.haze.common.model.network.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import net.zackzhang.code.haze.common.util.NumberUtils

class IntRangeAdapter : TypeAdapter<IntRange?>() {

    override fun write(out: JsonWriter, value: IntRange?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.jsonValue(NumberUtils.presentIntRange(value))
        }
    }

    override fun read(`in`: JsonReader): IntRange? {
        if (`in`.peek() != JsonToken.STRING) {
            `in`.skipValue()
            return null
        }
        return NumberUtils.parseIntRange(`in`.nextString())
    }
}