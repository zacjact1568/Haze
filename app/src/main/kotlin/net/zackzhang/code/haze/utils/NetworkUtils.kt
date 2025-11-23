package net.zackzhang.code.haze.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.zackzhang.code.util.check
import net.zackzhang.code.util.require
import net.zackzhang.code.util.requireNotNull
import net.zackzhang.code.util.wLog
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KClass

fun <T : Any> Gson.fromJsonObject(obj: JSONObject, cls: KClass<T>) =
    requireNotNull(
        fromJson(obj.toString(), cls.java),
        "`obj` is null or empty",
    )

fun <T : Any> Gson.fromJsonArray(arr: JSONArray, elmCls: KClass<T>): List<T> {
    val type = TypeToken.getParameterized(List::class.java, elmCls.java).type
    return requireNotNull(
        fromJson(arr.toString(), type),
        "`arr` is null or empty",
    )
}

fun <T : Any> Gson.fromJsonObject(obj: JSONObject, name: String, elmCls: KClass<T>) =
    runCatching {
        fromJsonArray(obj.getJSONArray(name), elmCls)
    }.onFailure {
        wLog(this::class, "getList", it.toString())
    }.getOrNull()

fun ResponseBody.toJsonObject() = JSONObject(string()).apply {
    val code = getInt("code")
    require(code != 404, "Not found")
    check(code == 200, "Failed")
}

fun JSONObject.getUpdateTime() = parseDateTime(getString("updateTime"))