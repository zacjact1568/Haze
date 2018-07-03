package net.zackzhang.app.cold.model.network.bean

import com.google.gson.annotations.SerializedName

/** 和风天气 API 接口数据 */
// 按 API 组织
class HeWeatherApi {

    /** 常规天气数据 */
    data class Weather(
            @SerializedName("HeWeather6")
            val heWeather6: List<HeWeather6>
    ) {
        data class HeWeather6(
                val basic: Basic,
                val update: Update,
                val status: String,
                val now: Now,
                @SerializedName("daily_forecast")
                val dailyForecast: List<DailyForecast>,
                val hourly: List<Hourly>,
                val lifestyle: List<Lifestyle>?
                // sunrise_sunset 没有可用值，指向 daily_forecast 中的 sr 和 ss
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
                    val tz: String
            )

            data class Update(
                    val loc: String,
                    val utc: String
            )

            data class Now(
                    val cloud: String,
                    @SerializedName("cond_code")
                    val condCode: String,
                    @SerializedName("cond_txt")
                    val condTxt: String,
                    val fl: String,
                    val hum: String,
                    val pcpn: String,
                    val pres: String,
                    val tmp: String,
                    val vis: String,
                    @SerializedName("wind_deg")
                    val windDeg: String,
                    @SerializedName("wind_dir")
                    val windDir: String,
                    @SerializedName("wind_sc")
                    val windSc: String,
                    @SerializedName("wind_spd")
                    val windSpd: String
            )

            data class DailyForecast(
                    @SerializedName("cond_code_d")
                    val condCodeD: String,
                    @SerializedName("cond_code_n")
                    val condCodeN: String,
                    @SerializedName("cond_txt_d")
                    val condTxtD: String,
                    @SerializedName("cond_txt_n")
                    val condTxtN: String,
                    val date: String,
                    val hum: String,
                    val mr: String,
                    val ms: String,
                    val pcpn: String,
                    val pop: String,
                    val pres: String,
                    val sr: String,
                    val ss: String,
                    @SerializedName("tmp_max")
                    val tmpMax: String,
                    @SerializedName("tmp_min")
                    val tmpMin: String,
                    val uv_index: String,
                    val vis: String,
                    @SerializedName("wind_deg")
                    val windDeg: String,
                    @SerializedName("wind_dir")
                    val windDir: String,
                    @SerializedName("wind_sc")
                    val windSc: String,
                    @SerializedName("wind_spd")
                    val windSpd: String
            )

            data class Hourly(
                    val cloud: String,
                    @SerializedName("cond_code")
                    val condCode: String,
                    @SerializedName("cond_txt")
                    val condTxt: String,
                    val dew: String,
                    val hum: String,
                    val pop: String,
                    val pres: String,
                    val time: String,
                    val tmp: String,
                    @SerializedName("wind_deg")
                    val windDeg: String,
                    @SerializedName("wind_dir")
                    val windDir: String,
                    @SerializedName("wind_sc")
                    val windSc: String,
                    @SerializedName("wind_spd")
                    val windSpd: String
            )

            data class Lifestyle(
                    val brf: String,
                    val txt: String,
                    val type: String
            )
        }
    }

    /** 空气质量数据 */
    data class Air(
            @SerializedName("HeWeather6")
            val heWeather6: List<HeWeather6>
    ) {
        // 县级城市返回的 status 为 permission denied，其他为空
        data class HeWeather6(
                val basic: Basic?,
                val update: Update?,
                val status: String,
                @SerializedName("air_now_city")
                val airNowCity: AirNowCity?,
                @SerializedName("air_now_station")
                val airNowStation: List<AirNowStation>?
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
                    val tz: String
            )

            data class Update(
                    val loc: String,
                    val utc: String
            )

            data class AirNowCity(
                    val aqi: String,
                    val qlty: String,
                    val main: String,
                    val pm25: String,
                    val pm10: String,
                    val no2: String,
                    val so2: String,
                    val co: String,
                    val o3: String,
                    @SerializedName("pub_time")
                    val pubTime: String
            )

            data class AirNowStation(
                    @SerializedName("air_sta")
                    val airSta: String,
                    val aqi: String,
                    val asid: String,
                    val co: String,
                    val lat: String,
                    val lon: String,
                    val main: String,
                    val no2: String,
                    val o3: String,
                    val pm10: String,
                    val pm25: String,
                    @SerializedName("pub_time")
                    val pubTime: String,
                    val qlty: String,
                    val so2: String
            )
        }
    }
}
