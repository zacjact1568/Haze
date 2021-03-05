package net.zackzhang.code.haze.model.bean

import java.util.*

data class Weather(
        val city: City,
        val current: Current = Current(),
        // time/date 必须不同，不然无法存进数据库，因此先放数组下标占位
        val hourlyForecasts: Array<HourlyForecast> = Array(HOURLY_FORECAST_LENGTH) { HourlyForecast(it.toLong()) },
        val dailyForecasts: Array<DailyForecast> = Array(DAILY_FORECAST_LENGTH) { DailyForecast(it.toLong()) },
        var updateTime: Long = 0L,
        // 定位城市为 0，用户添加的城市为当前时间
        var addTime: Long = System.currentTimeMillis(),
        // 当前更新状态
        var status: Int = STATUS_GENERAL
) {

    companion object {

        // 每 3 小时，3 * 8 = 24
        const val HOURLY_FORECAST_LENGTH = 8
        const val DAILY_FORECAST_LENGTH = 7
        // 温度趋势图中只显示 5 个
        const val DAILY_FORECAST_LENGTH_DISPLAY = 5

        /** 状态：正在更新 */
        const val STATUS_UPDATING = 1
        /** 状态：未在更新 */
        const val STATUS_GENERAL = 0
        /** 状态：已删除 */
        const val STATUS_DELETED = -1
    }

    // 在用户手动添加城市或 app 添加定位城市的时候使用
    // 从数据库中载入城市使用主构造函数
    constructor(
            cityId: String,
            cityName: String,
            isPrefectureCity: Boolean = false,
            isLocationCity: Boolean = false
    ) : this(City(cityId, cityName, isPrefectureCity)) {
        if (isLocationCity) addTime = 0L
    }

    val cityId
        get() = city.id

    val cityName
        get() = city.name

    val isPrefecture
        get() = city.isPrefecture

    val isNewAdded
        get() = updateTime == 0L

    // TODO 若上一次刷新时间过久，自动刷新；若上一次刷新时间过近，不执行刷新
    // val isUpdatedLongAgo

    val isUpdating
        get() = status == STATUS_UPDATING

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Weather

        if (city != other.city) return false
        if (current != other.current) return false
        if (!Arrays.equals(hourlyForecasts, other.hourlyForecasts)) return false
        if (!Arrays.equals(dailyForecasts, other.dailyForecasts)) return false
        if (updateTime != other.updateTime) return false
        if (addTime != other.addTime) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = city.hashCode()
        result = 31 * result + current.hashCode()
        result = 31 * result + Arrays.hashCode(hourlyForecasts)
        result = 31 * result + Arrays.hashCode(dailyForecasts)
        result = 31 * result + updateTime.hashCode()
        result = 31 * result + addTime.hashCode()
        result = 31 * result + status
        return result
    }

    data class City(
            var id: String = "",
            var name: String = "",
            var isPrefecture: Boolean = false
    )

    data class Current(
            var conditionCode: Int = 0,
            var temperature: Int = 0,
            var feelsLike: Int = 0,
            var airQualityIndex: Int = 0
    )

    data class HourlyForecast(
            var time: Long = 0L,
            var temperature: Int = 0,
            var precipitationProbability: Int = 0
    )

    data class DailyForecast(
            var date: Long = 0L,
            var temperatureMax: Int = 0,
            var temperatureMin: Int = 0,
            var conditionCodeDay: Int = 0,
            // 暂时不显示
            var conditionCodeNight: Int = 0,
            var precipitationProbability: Int = 0
    )
}
