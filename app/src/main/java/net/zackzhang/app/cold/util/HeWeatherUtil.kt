package net.zackzhang.app.cold.util

import android.util.Base64
import net.zackzhang.app.cold.App
import net.zackzhang.app.cold.R
import net.zackzhang.app.cold.common.Constant
import net.zackzhang.app.cold.exception.HeWeatherServiceException
import net.zackzhang.app.cold.model.network.bean.HeWeatherApi
import net.zackzhang.app.cold.model.bean.Weather
import net.zackzhang.app.cold.model.network.bean.HeWeatherSearch
import net.zackzhang.app.cold.model.network.service.HeWeatherApiService
import net.zackzhang.app.cold.model.network.service.HeWeatherSearchService
import java.security.MessageDigest

object HeWeatherUtil {

    fun parseWeatherAndAirData(hwaWeather: HeWeatherApi.Weather, hwaAir: HeWeatherApi.Air, weather: Weather) {

        parseWeatherData(hwaWeather, weather)

        val airApi = hwaAir.heWeather6[0]

        val airStatus = airApi.status
        if (airStatus != "ok") {
            // air 请求出错，抛出异常
            throw HeWeatherServiceException(HeWeatherApiService.TYPE_AIR, airStatus, convertStatusToInfo(airStatus))
        }

        // 若 air 请求出错，不更改 airQualityIndex 的值，默认为 0
        val airNowCity = airApi.airNowCity
        if (airNowCity != null) {
            weather.current.airQualityIndex = airNowCity.aqi.toInt()
        }

    }

    fun parseWeatherData(hwaWeather: HeWeatherApi.Weather, weather: Weather) {
        val weatherApi = hwaWeather.heWeather6[0]

        val weatherStatus = weatherApi.status
        if (weatherStatus != "ok") {
            // weather 请求出错，抛出异常
            throw HeWeatherServiceException(HeWeatherApiService.TYPE_WEATHER, weatherStatus, convertStatusToInfo(weatherStatus))
        }

        weather.updateTime = TimeUtil.parseTime(weatherApi.update.loc)

        val now = weatherApi.now
        val current = weather.current
        current.conditionCode = now.condCode.toInt()
        current.temperature = now.tmp.toInt()
        current.feelsLike = now.fl.toInt()

        for (i in 0 until Weather.HOURLY_FORECAST_LENGTH) {
            val hourlyForecastApi = weatherApi.hourly[i]
            val hourlyForecast = weather.hourlyForecasts[i]
            hourlyForecast.time = TimeUtil.parseTime(hourlyForecastApi.time)
            hourlyForecast.temperature = hourlyForecastApi.tmp.toInt()
            hourlyForecast.precipitationProbability = hourlyForecastApi.pop.toInt()
        }

        for (i in 0 until Weather.DAILY_FORECAST_LENGTH) {
            val dailyForecastApi = weatherApi.dailyForecast[i]
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

    fun makeApiSignature(paramsMap: Map<String, String>): String {
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

    /** 生成和风天气请求的数字签名 */
    fun makeApiSignature(cityId: String, time: String) = makeApiSignature(mapOf(
            "location" to cityId,
            "username" to Constant.HE_WEATHER_USER_ID,
            "t" to time
    ))

    /** 数字签名使用的时间 */
    val apiSignatureTime
        get() = (System.currentTimeMillis() / 1000).toString()

    fun parseCity(hwsFind: HeWeatherSearch.Find, city: Weather.City) {
        val findApi = hwsFind.heWeather6[0]
        // 检查状态
        val findStatus = findApi.status
        if (findStatus != "ok") {
            throw HeWeatherServiceException(HeWeatherSearchService.TYPE_FIND, findStatus, convertStatusToInfo(findStatus))
        }
        // 通过坐标获取的城市只有一个
        val (cid, location, parentCity) = findApi.basic[0]
        city.id = cid
        // 定位城市的城市名不是从本地数据库中获取的，而是使用网络返回的结果
        // 因为普通城市是从数据库的搜索结果中获取的城市 id 与名称
        city.name = location
        // 城市名与地级市相同时，该城市就是地级市
        city.isPrefecture = location == parentCity
    }

    /** 将状态码转换为当前语言的描述信息 */
    fun parseStatus(status: String): Int {
        val resId = ResourceUtil.getStringResourceId("error_heweather_status_${status.replace(' ', '_')}")
        if (resId == 0) throw IllegalArgumentException("Unknown status")
        return resId
    }

    /** 将状态码转换为描述信息 */
    private fun convertStatusToInfo(status: String) = when (status) {
        "invalid key" -> "Key 错误"
        // 官网上是 unknown location
        "unknown city" -> "未知或错误的城市或地区"
        "no data for this location" -> "该城市或地区没有所请求的数据"
        "no more requests" -> "超过总请求次数"
        "param invalid" -> "请求参数错误"
        "too fast" -> "请求频率过高"
        "dead" -> "接口服务异常"
        "permission denied" -> "无访问权限"
        "sign error" -> "认证签名错误"
        else -> throw IllegalArgumentException("Unknown status")
    }
}