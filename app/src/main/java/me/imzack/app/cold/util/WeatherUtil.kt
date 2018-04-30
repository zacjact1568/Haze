package me.imzack.app.cold.util

import android.util.Base64
import me.imzack.app.cold.App
import me.imzack.app.cold.R
import me.imzack.app.cold.common.Constant
import me.imzack.app.cold.model.bean.HeWeather
import me.imzack.app.cold.model.bean.Weather
import java.security.MessageDigest

object WeatherUtil {

    fun parseHeWeather(heWeather: HeWeather, weather: Weather) {
        val commonApi = heWeather.common!!.heWeather6[0]
        val airApi = heWeather.air!!.heWeather6[0]

        weather.updateTime = TimeUtil.parseTime(commonApi.update.loc)

        val now = commonApi.now
        val current = weather.current
        current.conditionCode = now.condCode.toInt()
        current.temperature = now.tmp.toInt()
        current.feelsLike = now.fl.toInt()
        // TODO 测试：是否某些城市没有空气数据
        current.airQualityIndex = airApi.airNowCity.aqi.toInt()

        for (i in 0 until Weather.HOURLY_FORECAST_LENGTH) {
            val hourlyForecastApi = commonApi.hourly[i]
            val hourlyForecast = weather.hourlyForecasts[i]
            hourlyForecast.time = TimeUtil.parseTime(hourlyForecastApi.time)
            hourlyForecast.temperature = hourlyForecastApi.tmp.toInt()
            hourlyForecast.precipitationProbability = hourlyForecastApi.pop.toInt()
        }

        for (i in 0 until Weather.DAILY_FORECAST_LENGTH) {
            val dailyForecastApi = commonApi.dailyForecast[i]
            val dailyForecast = weather.dailyForecasts[i]
            dailyForecast.date = TimeUtil.parseDate(dailyForecastApi.date)
            dailyForecast.temperatureMax = dailyForecastApi.tmpMax.toInt()
            dailyForecast.temperatureMin = dailyForecastApi.tmpMin.toInt()
            dailyForecast.conditionCodeDay = dailyForecastApi.condCodeD.toInt()
            dailyForecast.conditionCodeNight = dailyForecastApi.condCodeN.toInt()
            dailyForecast.precipitationProbability = dailyForecastApi.pop.toInt()
        }
    }

    /** 将空气质量指数转换成对应的等级 */
    fun parseAqi(aqi: Int) = App.context.resources.getString(when (aqi) {
        in 0..50 -> R.string.aqi_level_1
        in 51..100 -> R.string.aqi_level_2
        in 101..150 -> R.string.aqi_level_3
        in 151..200 -> R.string.aqi_level_4
        in 201..300 -> R.string.aqi_level_5
        in 301..Int.MAX_VALUE -> R.string.aqi_level_6
        else -> throw IllegalArgumentException("AQI should not be a negative number")
    })!!

    /** 生成和风天气请求的数字签名 */
    fun makeHeWeatherApiSignature(paramsMap: Map<String, String>): String {
        val params = StringBuilder()
        for ((key, value) in paramsMap.toSortedMap()) {
            params.append("$key=$value&")
        }
        // 删掉最后一个“&”，附上密钥
        params.deleteCharAt(params.lastIndex).append(Constant.HE_WEATHER_API_KEY)
        // 使用 NO_WRAP 是为了不在结尾添加换行符
        // 不需要 encode URL，因为 Retrofit 会自动 encode
        return Base64.encodeToString(MessageDigest.getInstance("MD5").digest(params.toString().toByteArray()), Base64.NO_WRAP)
    }
}