package me.imzack.app.cold.model.bean

/**
 * 将所有数据库表对应的类集合起来
 * @param status 当前状态
 */
data class Weather(
        val basic: Basic,
        val current: Current,
        val hourlyForecasts: Array<HourlyForecast>,
        val dailyForecasts: Array<DailyForecast>,
        var status: Int = STATUS_GENERAL
) {

    companion object {

        // 每3小时，3*8=24
        const val HOURLY_FORECAST_LENGTH = 8
        const val DAILY_FORECAST_LENGTH = 7
        /** 温度趋势图中只显示5个 */
        const val DAILY_FORECAST_LENGTH_DISPLAY = 5

        const val STATUS_ON_UPDATING = 1
        const val STATUS_GENERAL = 0
        const val STATUS_DELETED = -1
    }

    constructor(cityId: String, cityName: String) : this(
            Basic(cityId, cityName),
            Current(cityId),
            Array(HOURLY_FORECAST_LENGTH, { HourlyForecast(cityId, it) }),
            Array(DAILY_FORECAST_LENGTH, { DailyForecast(cityId, it) })
    )

    // 可从已有字段中获取，不再添加到构造函数
    val cityId
        get() = basic.cityId

    val cityName
        get() = basic.cityName

    val isOnUpdating
        get() = status == STATUS_ON_UPDATING
}
