package net.zackzhang.code.haze.weather.viewmodel

import android.graphics.Color
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.view.ThemeEntity
import net.zackzhang.code.haze.common.viewmodel.Event
import net.zackzhang.code.haze.common.viewmodel.BaseViewModel
import net.zackzhang.code.haze.weather.model.local.WeatherLocalRepository
import net.zackzhang.code.haze.weather.model.remote.WeatherRemoteRepository
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.common.constant.EVENT_CITY_LOADED
import net.zackzhang.code.haze.common.constant.EVENT_NETWORK_FAILED
import net.zackzhang.code.haze.common.constant.EVENT_THEME_CHANGED
import net.zackzhang.code.haze.common.util.formatTime
import net.zackzhang.code.haze.common.util.formatWeek
import net.zackzhang.code.haze.common.util.getAppColorRes
import net.zackzhang.code.haze.common.util.getString
import net.zackzhang.code.haze.common.util.plusAssign
import net.zackzhang.code.haze.common.util.presentIntRange
import net.zackzhang.code.haze.common.util.supportShorterExpression
import net.zackzhang.code.haze.common.util.toPrettifiedRelativeToNow
import net.zackzhang.code.haze.common.util.toStringOrPlaceholder
import net.zackzhang.code.haze.common.viewmodel.data.SourceCardData
import net.zackzhang.code.haze.weather.model.entity.WeatherEntity
import net.zackzhang.code.haze.weather.util.getTemperatureRange
import net.zackzhang.code.haze.weather.util.getConditionColorByCode
import net.zackzhang.code.haze.weather.util.getConditionIconResByCode
import net.zackzhang.code.haze.weather.viewmodel.data.*
import java.time.ZoneId

class WeatherViewModel : BaseViewModel() {

    private val entityLiveData by lazy {
        MutableLiveData<WeatherEntity>()
    }

    private val cardLiveData by lazy {
        Transformations.map(entityLiveData) { it.toCardDataList() }
    }

    fun observeCard(owner: LifecycleOwner, observer: (List<BaseCardData>) -> Unit) {
        cardLiveData.observe(owner, observer)
    }

    fun notifyLoadingData() {
        if (entityLiveData.value != null) return
        viewModelScope.launch {
            val weatherList = WeatherLocalRepository.query()
            if (weatherList.isNotEmpty()) {
                // 本地数据库中有缓存
                val weatherLocal = weatherList.first()
                eventLiveData.value = Event(EVENT_CITY_LOADED, weatherLocal.city)
                entityLiveData.value = WeatherEntity.fromWeatherLocalEntity(weatherLocal)
            } else {
                // 要先设置主题色，再 EVENT_DATA_LOADED
                // 否则打开城市页不会带着主题色
                setupTheme(null)
                eventLiveData.value = Event(EVENT_CITY_LOADED, null)
            }
        }
    }

    fun notifyRefreshing(cityId: String? = null) {
        val id = cityId ?: (entityLiveData.value ?: return).now.cityId
        viewModelScope.launch {
            val weather = WeatherRemoteRepository.getWeather(id)
            if (weather != null) {
                WeatherLocalRepository.insert(weather, cityId == null)
                entityLiveData.value = weather
            } else {
                eventLiveData.value = Event(EVENT_NETWORK_FAILED, null)
            }
        }
    }

    private fun WeatherEntity.toCardDataList(): List<BaseCardData> {
        val theme = setupTheme(now.conditionCode)
        val list = mutableListOf<BaseCardData>()
        list += WeatherHeadCardData(
            now.temperature,
            todayTemperatureRange,
            now.conditionName,
            air?.now?.category,
            updatedAt?.toPrettifiedRelativeToNow(),
            theme,
        )
        list += WeatherTitleCardData("温度趋势")
        list += toHourlyCardRowData()
        list += toDailyCardDataList()
        list += WeatherTitleCardData("实况数据")
        list += toCurrentCardDataList()
        list += SourceCardData()
        return list
    }

    private fun WeatherEntity.toHourlyCardRowData() =
        WeatherHourlyRowCardData(
            hourly.mapTo(
                mutableListOf(
                    WeatherHourlyCardData(
                        getString(R.string.weather_hourly_item_time_now),
                        getConditionIconResByCode(now.conditionCode),
                        now.temperature.toStringOrPlaceholder()
                    )
                )
            ) {
                WeatherHourlyCardData(
                    // 换算成了用户所在地的时间，而不是当地时间
                    // TODO 提供选项
                    it.time.withZoneSameInstant(ZoneId.systemDefault()).formatTime(),
                    getConditionIconResByCode(it.conditionCode),
                    it.temperature.toStringOrPlaceholder()
                )
            }
        )

    private fun WeatherEntity.toDailyCardDataList() =
        daily.mapIndexed { index, entity ->
            WeatherDailyCardData(
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
                entity.getTemperatureRange(),
                temperatureRangeAmongAllDates,
            )
        }

    private fun WeatherEntity.toCurrentCardDataList() =
        listOf(
            WeatherCurrentCardData(R.drawable.ic_temperature_half, getAppColorRes(R.color.red_500), now.temperature?.toString(), "气温"),
            WeatherCurrentCardData(getConditionIconResByCode(now.conditionCode), getAppColorRes(R.color.blue_500), now.conditionName, "天气状况", 1.4F),
            WeatherCurrentCardData(R.drawable.ic_leaf, getAppColorRes(R.color.yellow_500), now.feelsLike?.toString(), "体感温度"),
            WeatherCurrentCardData(R.drawable.ic_water, getAppColorRes(R.color.blue_500), now.humidity?.toString(), "湿度"),
            WeatherCurrentCardData(R.drawable.ic_tree, getAppColorRes(R.color.green_500), air?.now?.category, "空气质量"),
            WeatherCurrentCardData(R.drawable.ic_lungs, getAppColorRes(R.color.pink_700), air?.now?.primary, "主要污染物"),
            WeatherCurrentCardData(R.drawable.ic_droplet, getAppColorRes(R.color.light_blue_200), now.precipitation?.toString(), "降水量"),
            WeatherCurrentCardData(R.drawable.ic_panel, getAppColorRes(R.color.deep_purple_400), now.pressure?.toString(), "气压"),
            WeatherCurrentCardData(R.drawable.ic_eye, getAppColorRes(R.color.brown_300), now.visibility?.toString(), "能见度"),
            WeatherCurrentCardData(R.drawable.ic_wind, getAppColorRes(R.color.teal_a200), presentIntRange(now.windScale), "风力"),
            WeatherCurrentCardData(R.drawable.ic_fan, getAppColorRes(R.color.light_green_400), now.windDirection, "风向"),
            WeatherCurrentCardData(R.drawable.ic_cloud, getAppColorRes(R.color.cyan_300), now.cloud?.toString(), "云量"),
        )

    private fun setupTheme(conditionCode: Int?): ThemeEntity {
        val theme = ThemeEntity(
            getConditionColorByCode(conditionCode),
            // 前景色暂时固定为白色
            Color.WHITE,
        )
        eventLiveData.value = Event(EVENT_THEME_CHANGED, theme)
        return theme;
    }
}