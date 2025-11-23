package net.zackzhang.code.haze.models

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import net.zackzhang.code.haze.BuildConfig
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.components.recycler.cards.CardConfigBase
import net.zackzhang.code.haze.components.recycler.cards.SourceCardConfig
import net.zackzhang.code.haze.components.recycler.cards.WeatherCurrentCardConfig
import net.zackzhang.code.haze.components.recycler.cards.WeatherDailyCardConfig
import net.zackzhang.code.haze.components.recycler.cards.WeatherHeadCardConfig
import net.zackzhang.code.haze.components.recycler.cards.WeatherHourlyCardConfig
import net.zackzhang.code.haze.components.recycler.cards.WeatherHourlyRowCardConfig
import net.zackzhang.code.haze.components.recycler.cards.WeatherTitleCardConfig
import net.zackzhang.code.haze.models.sources.AirNowEntity
import net.zackzhang.code.haze.models.sources.AirServices
import net.zackzhang.code.haze.models.sources.AppDatabaseInstance
import net.zackzhang.code.haze.models.sources.CityWeatherEntity
import net.zackzhang.code.haze.models.sources.WeatherServices
import net.zackzhang.code.haze.models.sources.WeatherDailyEntity
import net.zackzhang.code.haze.utils.formatTime
import net.zackzhang.code.haze.utils.formatWeek
import net.zackzhang.code.haze.utils.getAppColorRes
import net.zackzhang.code.haze.utils.getString
import net.zackzhang.code.haze.utils.presentIntRange
import net.zackzhang.code.haze.utils.supportShorterExpression
import net.zackzhang.code.haze.utils.toPrettifiedRelativeToNow
import net.zackzhang.code.haze.utils.toStringOrPlaceholder
import net.zackzhang.code.haze.models.sources.WeatherHourlyEntity
import net.zackzhang.code.haze.models.sources.WeatherLocalEntity
import net.zackzhang.code.haze.utils.getConditionColorByCode
import net.zackzhang.code.haze.utils.getConditionIconResByCode
import net.zackzhang.code.haze.utils.makeQWeatherSignature
import net.zackzhang.code.haze.utils.plusAssign
import net.zackzhang.code.haze.utils.seconds
import net.zackzhang.code.util.eLog
import java.time.ZoneId
import kotlin.collections.forEach
import kotlin.math.max
import kotlin.math.min

class WeatherViewModel : ModelBase() {

    private val entityLiveData by lazy {
        MutableLiveData<WeatherEntity>()
    }

    private val cardLiveData by lazy {
        entityLiveData.map { it.toCardConfigs() }
    }

    fun observeCard(owner: LifecycleOwner, observer: (List<CardConfigBase>) -> Unit) {
        cardLiveData.observe(owner, observer)
    }

    fun notifyLoadingData() {
        if (entityLiveData.value != null) return
        viewModelScope.launch {
            val weatherList = AppDatabaseInstance.weatherDao().query()
            if (weatherList.isNotEmpty()) {
                // 本地数据库中有缓存
                val weatherLocal = weatherList.first()
                notifyEvent(CityLoadedEvent(weatherLocal.city))
                entityLiveData.value = WeatherEntity.fromWeatherLocalEntity(weatherLocal)
            } else {
                // 要先设置主题色，再 EVENT_DATA_LOADED
                // 否则打开城市页不会带着主题色
                setupTheme(null)
                notifyEvent(CityLoadedEvent(null))
            }
        }
    }

    fun notifyRefreshing(cityId: String? = null) {
        val id = cityId ?: (entityLiveData.value ?: return).now.cityId
        viewModelScope.launch {
            val weather = getWeather(id)
            if (weather != null) {
                // Room 的协程实现调用了线程池，无需使用 Dispatchers.IO
                // See DefaultTaskExecutor#mDiskIO
                val dao = AppDatabaseInstance.weatherDao()
                if (cityId == null) {
                    dao.replace(weather.cityId, weather.dbHourly, weather.dbDaily, weather.dbAir)
                } else {
                    dao.insert(weather.dbHourly, weather.dbDaily, weather.dbAir)
                }
                entityLiveData.value = weather
            } else {
                notifyEvent(Event.NetworkFailed)
            }
        }
    }

    /**
     * 网络获取天气数据，失败返回 null
     * SupervisorJob 是为了阻止异常向上层协程传递
     * Retrofit 的协程实现调用了 enqueue，无需使用 Dispatchers.IO
     */
    private suspend fun getWeather(cityId: String) = supervisorScope {
        val time = seconds
        val sign = makeQWeatherSignature(cityId, time)
        val now = async {
            WeatherServices.getNow(
                cityId,
                BuildConfig.QWEATHER_PUBLIC_ID,
                time,
                sign,
            )
        }
        val hourly = async {
            WeatherServices.getHourly(
                cityId,
                BuildConfig.QWEATHER_PUBLIC_ID,
                time,
                sign,
            )
        }
        val daily = async {
            WeatherServices.getDaily(
                cityId,
                BuildConfig.QWEATHER_PUBLIC_ID,
                time,
                sign,
            )
        }
        val air = async {
            // 国内城市才有空气质量数据
            // 暂时使用 cityID 以 10 开头判断
            if (cityId.startsWith("10")) {
                AirServices.getNow(
                    cityId,
                    BuildConfig.QWEATHER_PUBLIC_ID,
                    time,
                    sign,
                )
            } else null
        }
        runCatching {
            WeatherEntity(now.await(), hourly.await(), daily.await(), air.await())
        }.onSuccess {
            it.attachCityId(cityId)
        }.onFailure {
            eLog(this::class, "getWeather", "onFailure: ${it.message}")
            now.cancel()
            hourly.cancel()
            daily.cancel()
            air.cancel()
        }.getOrNull()
    }

    private fun WeatherEntity.toCardConfigs(): List<CardConfigBase> {
        setupTheme(now.conditionCode)
        val list = mutableListOf<CardConfigBase>()
        list += WeatherHeadCardConfig(
            now.temperature,
            todayTemperatureRange,
            now.conditionName,
            air?.now?.category,
            updatedAt?.toPrettifiedRelativeToNow(),
        )
        list += WeatherTitleCardConfig("温度趋势")
        list += toHourlyCardRowData()
        list += toDailyCardConfigs()
        list += WeatherTitleCardConfig("实况数据")
        list += toCurrentCardConfigs()
        list += SourceCardConfig()
        return list
    }

    private fun WeatherEntity.toHourlyCardRowData() =
        WeatherHourlyRowCardConfig(
            hourly.mapTo(
                mutableListOf(
                    WeatherHourlyCardConfig(
                        getString(R.string.weather_hourly_item_time_now),
                        getConditionIconResByCode(now.conditionCode),
                        now.temperature.toStringOrPlaceholder()
                    )
                )
            ) {
                WeatherHourlyCardConfig(
                    // 换算成了用户所在地的时间，而不是当地时间
                    // TODO 提供选项
                    it.time.withZoneSameInstant(ZoneId.systemDefault()).formatTime(),
                    getConditionIconResByCode(it.conditionCode),
                    it.temperature.toStringOrPlaceholder()
                )
            }
        )

    private fun WeatherEntity.toDailyCardConfigs() =
        daily.mapIndexed { index, entity ->
            WeatherDailyCardConfig(
                when (index) {
                    0 -> getString(R.string.weather_daily_item_time_today)
                    1 -> {
                        if (supportShorterExpression) {
                            getString(R.string.weather_daily_item_time_tomorrow)
                        } else {
                            entity.date.formatWeek()
                        }
                    }
                    else -> entity.date.formatWeek()
                },
                // TODO support night condition
                getConditionIconResByCode(entity.conditionCodeDay),
                now.temperature,
                entity.temperatureRange,
                temperatureRangeAmongAllDates,
            )
        }

    private fun WeatherEntity.toCurrentCardConfigs() =
        listOf(
            WeatherCurrentCardConfig(
                R.drawable.ic_temperature_half,
                getAppColorRes(R.color.red_500),
                now.temperature?.toString(),
                "气温",
            ),
            WeatherCurrentCardConfig(
                getConditionIconResByCode(now.conditionCode),
                getAppColorRes(R.color.blue_500),
                now.conditionName,
                "天气状况",
                1.4F,
            ),
            WeatherCurrentCardConfig(
                R.drawable.ic_leaf,
                getAppColorRes(R.color.yellow_500),
                now.feelsLike?.toString(),
                "体感温度",
            ),
            WeatherCurrentCardConfig(
                R.drawable.ic_water,
                getAppColorRes(R.color.blue_500),
                now.humidity?.toString(),
                "湿度",
            ),
            WeatherCurrentCardConfig(
                R.drawable.ic_tree,
                getAppColorRes(R.color.green_500),
                air?.now?.category,
                "空气质量",
            ),
            WeatherCurrentCardConfig(
                R.drawable.ic_lungs,
                getAppColorRes(R.color.pink_700),
                air?.now?.primary,
                "主要污染物",
            ),
            WeatherCurrentCardConfig(
                R.drawable.ic_droplet,
                getAppColorRes(R.color.light_blue_200),
                now.precipitation?.toString(),
                "降水量",
            ),
            WeatherCurrentCardConfig(
                R.drawable.ic_panel,
                getAppColorRes(R.color.deep_purple_400),
                now.pressure?.toString(),
                "气压",
            ),
            WeatherCurrentCardConfig(
                R.drawable.ic_eye,
                getAppColorRes(R.color.brown_300),
                now.visibility?.toString(),
                "能见度",
            ),
            WeatherCurrentCardConfig(
                R.drawable.ic_wind,
                getAppColorRes(R.color.teal_a200),
                presentIntRange(now.windScale),
                "风力",
            ),
            WeatherCurrentCardConfig(
                R.drawable.ic_fan,
                getAppColorRes(R.color.light_green_400),
                now.windDirection,
                "风向",
            ),
            WeatherCurrentCardConfig(
                R.drawable.ic_cloud,
                getAppColorRes(R.color.cyan_300),
                now.cloud?.toString(),
                "云量",
            ),
        )

    private fun setupTheme(conditionCode: Int?) {
        val accentColor = getConditionColorByCode(conditionCode)
        notifyThemeChanged(ThemeEntity(accentColor))
    }
}

private data class WeatherEntity(
    val now: WeatherHourlyEntity,
    val hourly: List<WeatherHourlyEntity>,
    val daily: List<WeatherDailyEntity>,
    val air: AirEntity?,
) {

    companion object {

        fun fromWeatherLocalEntity(entity: WeatherLocalEntity) = WeatherEntity(
            entity.hourlyList.first { it.isNow },
            entity.hourlyList.filterNot { it.isNow },
            entity.dailyList,
            AirEntity.fromAirNowEntityList(entity.airList)
        )
    }

    val cityId get() = now.cityId

    val dbHourly: List<WeatherHourlyEntity>
        get() = mutableListOf(now).also { it += hourly }

    val dbDaily get() = daily

    val dbAir: List<AirNowEntity>
        get() = if (air == null) emptyList() else mutableListOf(air.now).also { it += air.stations }

    val todayTemperatureRange: IntRange?
        get() = if (daily.isEmpty()) null else daily[0].temperatureRange

    val temperatureRangeAmongAllDates: IntRange get() {
        var min = Int.MAX_VALUE
        var max = Int.MIN_VALUE
        daily.forEach {
            min = min(min, it.temperatureMin ?: min)
            max = max(max, it.temperatureMax ?: max)
        }
        return min..max
    }

    val updatedAt get() = now.updatedAt

    fun attachCityId(cityId: String) {
        now.cityId = cityId
        hourly.forEach { it.cityId = cityId }
        daily.forEach { it.cityId = cityId }
        air?.attachCityId(cityId)
    }
}

data class AirEntity(val now: AirNowEntity, val stations: List<AirNowEntity>?) {

    companion object {

        fun fromAirNowEntityList(list: List<AirNowEntity>) =
            if (list.isNotEmpty()) AirEntity(
                list.first { it.isNow },
                list.filterNot { it.isNow },
            ) else null
    }

    fun attachCityId(cityId: String) {
        now.cityId = cityId
        now.stationId = ""
        stations?.forEach { it.cityId = cityId }
    }
}

/**
 * 本地数据库缓存中的城市加载通知
 * 用于 WeatherFragment -> HomeActivity
 */
data class CityLoadedEvent(val entity: CityWeatherEntity?) : Event