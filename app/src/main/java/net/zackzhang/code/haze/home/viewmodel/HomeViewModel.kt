package net.zackzhang.code.haze.home.viewmodel

import net.zackzhang.code.haze.city.model.entity.CityWeatherEntity
import net.zackzhang.code.haze.base.viewmodel.Event
import net.zackzhang.code.haze.base.viewmodel.BaseViewModel
import net.zackzhang.code.haze.common.constant.EVENT_CITY_CHANGED
import net.zackzhang.code.haze.common.constant.EVENT_DATA_LOADED

class HomeViewModel : BaseViewModel() {

    // TODO 换成 notifyEvent
    var cityName: String? = null
        private set

    fun notifyDataLoaded(city: CityWeatherEntity) {
        cityName = city.name
        eventLiveData.value = Event(EVENT_DATA_LOADED, city)
    }

    fun notifyCityChanged(city: CityWeatherEntity) {
        cityName = city.name
        eventLiveData.value = Event(EVENT_CITY_CHANGED, city)
    }
}