package net.zackzhang.code.haze.weather.viewmodel

import android.graphics.Color
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.zackzhang.code.haze.common.constant.EVENT_DATA_LOADED
import net.zackzhang.code.haze.common.constant.EVENT_THEME_CHANGED
import net.zackzhang.code.haze.common.model.entity.ThemeEntity
import net.zackzhang.code.haze.common.util.toPrettifiedRelativeToNow
import net.zackzhang.code.haze.common.viewmodel.Event
import net.zackzhang.code.haze.common.viewmodel.EventViewModel
import net.zackzhang.code.haze.weather.model.local.WeatherLocalRepository
import net.zackzhang.code.haze.weather.model.remote.WeatherRemoteRepository
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.weather.model.entity.WeatherEntity
import net.zackzhang.code.haze.weather.util.getThemeColorByConditionCode
import net.zackzhang.code.haze.weather.viewmodel.data.WeatherHeadCardData

class WeatherViewModel : EventViewModel() {

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
            val weather = WeatherLocalRepository.query()
            weather.getOrNull(weather.size - 1)?.let {
                eventLiveData.value = Event(EVENT_DATA_LOADED, it.city)
                entityLiveData.value = WeatherEntity.fromWeatherLocalEntity(it)
            }
        }
    }

    fun notifyRefreshing(cityId: String? = null) {
        val id = cityId ?: (entityLiveData.value ?: return).now.cityId
        viewModelScope.launch {
            val weather = WeatherRemoteRepository.getWeather(id)
            WeatherLocalRepository.insert(weather, cityId == null)
            entityLiveData.value = weather
        }
    }

    private fun WeatherEntity.toCardDataList(): List<BaseCardData> {
        val theme = ThemeEntity(
            getThemeColorByConditionCode(now.conditionCode),
            // 前景色暂时固定为白色
            Color.WHITE,
        )
        eventLiveData.value = Event(EVENT_THEME_CHANGED, theme)
        return listOf(WeatherHeadCardData(
            now.temperature,
            todayTemperatureRange,
            now.conditionName,
            air.now.category,
            updatedAt?.toPrettifiedRelativeToNow(),
            theme,
        ))
    }
}