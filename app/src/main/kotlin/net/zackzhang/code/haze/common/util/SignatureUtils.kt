package net.zackzhang.code.haze.common.util

import net.zackzhang.code.haze.BuildConfig
import net.zackzhang.code.haze.common.constant.QWEATHER_PARAM_LOCATION
import net.zackzhang.code.haze.common.constant.QWEATHER_PARAM_PUBLIC_ID
import net.zackzhang.code.haze.common.constant.QWEATHER_PARAM_TIME
import java.security.MessageDigest

val QWEATHER_PUBLIC_ID get() = BuildConfig.QWEATHER_PUBLIC_ID

fun makeSignature(location: String, time: String): String {
    val params = StringBuilder()
    sortedMapOf(
        QWEATHER_PARAM_LOCATION to location,
        QWEATHER_PARAM_PUBLIC_ID to QWEATHER_PUBLIC_ID,
        QWEATHER_PARAM_TIME to time
    ).forEach { (k, v) -> params.append("$k=$v&") }
    // 删掉最后一个 &，附上密钥
    params.deleteCharAt(params.lastIndex).append(BuildConfig.QWEATHER_KEY)
    return MessageDigest.getInstance("MD5").digest(params.toString().toByteArray()).toHexString()
}