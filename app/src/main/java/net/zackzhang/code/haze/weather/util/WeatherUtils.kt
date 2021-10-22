package net.zackzhang.code.haze.weather.util

import androidx.annotation.ColorInt
import net.zackzhang.code.haze.HazeApplication
import net.zackzhang.code.haze.R

@ColorInt
fun getThemeColorByConditionCode(code: Int?) =
    HazeApplication.context.getColor(when (code) {
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
        else -> R.color.colorPrimary
    })