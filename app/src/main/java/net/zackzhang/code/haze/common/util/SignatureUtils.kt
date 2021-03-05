package net.zackzhang.code.haze.common.util

import net.zackzhang.code.haze.BuildConfig
import net.zackzhang.code.haze.common.Constants
import java.security.MessageDigest

object SignatureUtils {

    val time get() = (System.currentTimeMillis() / 1000).toString()

    fun make(location: String, time: String): String {
        val params = StringBuilder()
        sortedMapOf(
            Constants.QWEATHER_PARAM_LOCATION to location,
            Constants.QWEATHER_PARAM_PUBLIC_ID to BuildConfig.QWEATHER_PUBLIC_ID,
            Constants.QWEATHER_PARAM_TIME to time
        ).forEach { (k, v) -> params.append("$k=$v&") }
        // 删掉最后一个 &，附上密钥
        params.deleteCharAt(params.lastIndex).append(BuildConfig.QWEATHER_KEY)
        return MessageDigest.getInstance("MD5").digest(params.toString().toByteArray()).toHexString()
    }

    private fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
}