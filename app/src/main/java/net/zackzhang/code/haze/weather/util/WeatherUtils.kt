package net.zackzhang.code.haze.weather.util

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.util.context
import net.zackzhang.code.haze.common.util.dLog
import net.zackzhang.code.haze.weather.model.entity.WeatherDailyEntity

private val DEFAULT_CONDITION_COLOR = R.color.logo_theme_a

private val DEFAULT_CONDITION_ICON = R.drawable.ic_condition_999

@ColorRes
fun getConditionColorByCode(code: Int?) =
    when (code) {
        // 晴（白天）、热
        100, 900 -> R.color.accent_sunny
        // 有云（白天）
        in 101..103 -> R.color.accent_cloudy
        // 阴（白天）
        104 -> R.color.accent_overcast
        // 晴（夜晚）、月相
        150, in 800..807 -> R.color.accent_sunny_night
        // 有云（夜晚）
        in 151..153 -> R.color.accent_cloudy_night
        // 阴（夜晚）
        154 -> R.color.accent_overcast_night
        // 雨
        in 300..399 -> R.color.accent_rainy
        // 雪、冷
        in 400..499, 901 -> R.color.accent_snowy
        // 雾
        500, 501, 509, 510, 514, 515 -> R.color.accent_foggy
        // 霾、沙尘
        in 502..508, in 511..513 -> R.color.accent_hazy
        // 未知（999、null）
        else -> DEFAULT_CONDITION_COLOR
    }

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