package net.zackzhang.code.haze.utils

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import net.zackzhang.code.haze.R

@ColorRes
fun getConditionColorByCode(code: Int?) = when (code) {
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
    else -> R.color.launcher_icon_a
}

@DrawableRes
fun getConditionIconResByCode(code: Int?) = when (code) {
    100 -> R.drawable.ic_condition_100
    101 -> R.drawable.ic_condition_101
    102 -> R.drawable.ic_condition_102
    103 -> R.drawable.ic_condition_103
    104 -> R.drawable.ic_condition_104
    150 -> R.drawable.ic_condition_150
    in 151..153 -> R.drawable.ic_condition_151_152_153
    154 -> R.drawable.ic_condition_154
    200, in 202..204 -> R.drawable.ic_condition_200_202_203_204
    201 -> R.drawable.ic_condition_201
    in 205..207 -> R.drawable.ic_condition_205_206_207
    in 208..213 -> R.drawable.ic_condition_208_209_210_211_212_213
    300 -> R.drawable.ic_condition_300
    301 -> R.drawable.ic_condition_301
    302 -> R.drawable.ic_condition_302
    303 -> R.drawable.ic_condition_303
    304 -> R.drawable.ic_condition_304
    305 -> R.drawable.ic_condition_305
    306, 314 -> R.drawable.ic_condition_306_314
    307, 315 -> R.drawable.ic_condition_307_315
    308, 312, 318 -> R.drawable.ic_condition_308_312_318
    309 -> R.drawable.ic_condition_309
    310, 316 -> R.drawable.ic_condition_310_316
    311, 317 -> R.drawable.ic_condition_311_317
    313 -> R.drawable.ic_condition_313
    350 -> R.drawable.ic_condition_350
    351 -> R.drawable.ic_condition_351
    399 -> R.drawable.ic_condition_399
    400 -> R.drawable.ic_condition_400
    401, 408 -> R.drawable.ic_condition_401_408
    402, 409 -> R.drawable.ic_condition_402_409
    403, 410 -> R.drawable.ic_condition_403_410
    404 -> R.drawable.ic_condition_404
    405 -> R.drawable.ic_condition_405
    406 -> R.drawable.ic_condition_406
    407 -> R.drawable.ic_condition_407
    456 -> R.drawable.ic_condition_456
    457 -> R.drawable.ic_condition_457
    499 -> R.drawable.ic_condition_499
    500 -> R.drawable.ic_condition_500
    501 -> R.drawable.ic_condition_501
    502 -> R.drawable.ic_condition_502
    503 -> R.drawable.ic_condition_503
    504 -> R.drawable.ic_condition_504
    507 -> R.drawable.ic_condition_507
    508 -> R.drawable.ic_condition_508
    509, 510, 514, 515 -> R.drawable.ic_condition_509_510_514_515
    511 -> R.drawable.ic_condition_511
    512 -> R.drawable.ic_condition_512
    513 -> R.drawable.ic_condition_513
    900 -> R.drawable.ic_condition_900
    901 -> R.drawable.ic_condition_901
    999 -> R.drawable.ic_condition_999
    else -> R.drawable.ic_condition_999
}