package net.zackzhang.code.haze.models

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import net.zackzhang.code.haze.models.sources.CityWeatherEntity

class HomeViewModel : ModelBase() {

    val hasCity get() = cityLiveData.value != null

    private val cityLiveData by lazy {
        MutableLiveData<CityWeatherEntity?>()
    }

    fun observeCity(owner: LifecycleOwner, observer: (CityWeatherEntity?) -> Unit) {
        cityLiveData.observe(owner, observer)
    }

    fun notifyCityLoaded(city: CityWeatherEntity?) {
        cityLiveData.value = city
    }

    fun notifyCityChanged(city: CityWeatherEntity) {
        cityLiveData.value = city
        // 通知 WeatherFragment 获取数据 & 刷新列表
        notifyEvent(CityChangedEvent(city))
    }
}

/**
 * 城市更改通知
 * 用于 HomeActivity -> WeatherFragment
 */
data class CityChangedEvent(val city: CityWeatherEntity) : Event