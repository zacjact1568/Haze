package net.zackzhang.code.haze.core.weather.util

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import net.zackzhang.code.haze.core.R
import net.zackzhang.code.haze.base.util.context
import net.zackzhang.code.haze.base.util.dLog
import net.zackzhang.code.haze.core.weather.model.entity.WeatherDailyEntity

private val DEFAULT_CONDITION_COLOR = R.color.colorPrimary

private val DEFAULT_CONDITION_ICON = R.drawable.ic_condition_999

@ColorInt
fun getConditionColorByCode(code: Int?) =
    context.getColor(when (code) {
        // 晴（白天）、热
        100, 900 -> R.color.blue_200
        // 有云（白天）
        in 101..103 -> R.color.light_blue_200
        // 阴（白天）
        104 -> R.color.cyan_200
        // 晴（夜晚）、月相
        150, in 800..807 -> R.color.purple_200
        // 有云（夜晚）
        in 151..153 -> R.color.deep_purple_200
        // 阴（夜晚）
        154 -> R.color.indigo_200
        // 雨
        in 300..399 -> R.color.light_green_200
        // 雪、冷
        in 400..499, 901 -> R.color.green_200
        // 雾
        500, 501, 509, 510, 514, 515 -> R.color.teal_200
        // 霾、沙尘
        in 502..508, in 511..513 -> R.color.brown_200
        // 未知（999、null）
        else -> DEFAULT_CONDITION_COLOR
    })

fun WeatherDailyEntity.getTemperatureRange() =
    if (temperatureMin == null || temperatureMax == null) null else temperatureMin..temperatureMax

@DrawableRes
fun getConditionIconResByCode(code: Int?): Int {
    code ?: return DEFAULT_CONDITION_ICON
    val prefix = when (code) {
        in 151..153 -> "151_152_153"
        200, in 202..204 -> "200_202_203_204"
        in 205..207 -> "205_206_207"
        in 208..213 -> "208_209_210_211_212_213"
        306, 314 -> "306_314"
        307, 315 -> "307_315"
        308, 312, 318 -> "308_312_318"
        310, 316 -> "310_316"
        311, 317 -> "311_317"
        401, 408 -> "401_408"
        402, 409 -> "402_409"
        403, 410 -> "403_410"
        509, 510, 514, 515 -> "509_510_514_515"
        else -> code.toString()
    }
    dLog("getConditionIconResByCode($code), prefix: $prefix", "WeatherUtils")
    val resId = context.resources.getIdentifier("ic_condition_$prefix", "drawable", context.packageName)
    return if (resId == 0) DEFAULT_CONDITION_ICON else resId
}