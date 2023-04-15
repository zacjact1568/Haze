package net.zackzhang.code.haze.home.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import net.zackzhang.code.haze.core.city.model.entity.CityWeatherEntity
import net.zackzhang.code.haze.base.viewmodel.Event
import net.zackzhang.code.haze.base.viewmodel.BaseViewModel
import net.zackzhang.code.haze.common.constant.EVENT_CITY_CHANGED

class HomeViewModel : BaseViewModel() {

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
        eventLiveData.value = Event(EVENT_CITY_CHANGED, city)
    }
}