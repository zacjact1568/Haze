package net.zackzhang.code.haze.utils

import net.zackzhang.code.haze.BuildConfig
import java.security.MessageDigest

fun makeQWeatherSignature(location: String, time: String): String {
    val params = StringBuilder()
    sortedMapOf(
        "location" to location,
        "publicid" to BuildConfig.QWEATHER_PUBLIC_ID,
        "t" to time
    ).forEach { (k, v) -> params.append("$k=$v&") }
    // 删掉最后一个 &，附上密钥
    params.deleteCharAt(params.lastIndex).append(BuildConfig.QWEATHER_KEY)
    return MessageDigest.getInstance("MD5").digest(params.toString().toByteArray()).toHexString()
}