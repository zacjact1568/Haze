package me.imzack.app.cold.model.network.bean

import com.google.gson.annotations.SerializedName

/** 和风天气城市查询 API 接口数据 */
// 按 API 组织
class HeWeatherSearch {

    /** 城市搜索 */
    data class Find(
            @SerializedName("HeWeather6")
            val heWeather6: List<HeWeather6>
    ) {
        data class HeWeather6(
                val basic: List<Basic>,
                val status: String
        ) {
            data class Basic(
                    val cid: String,
                    val location: String,
                    @SerializedName("parent_city")
                    val parentCity: String,
                    @SerializedName("admin_area")
                    val adminArea: String,
                    val cnty: String,
                    val lat: String,
                    val lon: String,
                    val tz: String,
                    val type: String
            )
        }
    }
}
