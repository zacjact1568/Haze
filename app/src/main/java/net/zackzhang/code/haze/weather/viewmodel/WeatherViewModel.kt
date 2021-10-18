package net.zackzhang.code.haze.weather.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.zackzhang.code.haze.common.Constants
import net.zackzhang.code.haze.common.util.DateTimeUtils.toPrettifiedRelativeToNow
import net.zackzhang.code.haze.common.viewmodel.Event
import net.zackzhang.code.haze.common.viewmodel.EventViewModel
import net.zackzhang.code.haze.weather.model.local.WeatherLocalRepository
import net.zackzhang.code.haze.weather.model.remote.WeatherRemoteRepository
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.weather.model.entity.WeatherEntity
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
                eventLiveData.value = Event(Constants.EVENT_DATA_LOADED, it.city)
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
        return listOf(WeatherHeadCardData(
            now.temperature,
            now.conditionName,
            air.now.category,
            todayTemperatureRange,
            updatedAt?.toPrettifiedRelativeToNow(),
        ))
    }
}