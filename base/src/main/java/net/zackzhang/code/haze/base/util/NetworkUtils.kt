package net.zackzhang.code.haze.base.util

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import net.zackzhang.code.haze.base.exception.PlaceholderException
import net.zackzhang.code.haze.base.model.network.GsonTypeAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KClass

fun getOkHttpClient(tag: String) = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor { iLog(it, tag) }
        .setLevel(HttpLoggingInterceptor.Level.BODY))
    .addNetworkInterceptor(StethoInterceptor())
    .build()

private val GSON by lazy { GsonBuilder().registerTypeAdapterFactory(GsonTypeAdapterFactory()).create()!! }

@Throws(JsonSyntaxException::class, PlaceholderException::class)
fun <T : Any> fromJsonObject(obj: JSONObject, cls: KClass<T>): T {
    return GSON.fromJson(obj.toString(), cls.java) ?: throw PlaceholderException(0, "null")
}

@Throws(JsonParseException::class, JsonSyntaxException::class, PlaceholderException::class)
fun <T : Any> fromJsonArray(arr: JSONArray, elmCls: KClass<T>): List<T> {
    val type = TypeToken.getParameterized(List::class.java, elmCls.java).type
    return GSON.fromJson(arr.toString(), type) ?: throw PlaceholderException(0, "null")
}

fun <T : Any> JSONObject.getList(name: String, elmCls: KClass<T>) =
    runCatching {
        fromJsonArray(getJSONArray(name), elmCls)
    }.onFailure {
        wLog(it)
    }.getOrNull()

fun responseBodyToJsonObject(body: ResponseBody) = JSONObject(body.string()).apply {
    when (val code = getInt("code")) {
        200 -> {}
        else -> throw PlaceholderException(code, "Code is not 200")
    }
}

fun getUpdateTime(obj: JSONObject) = parseDateTime(obj.getString("updateTime"))