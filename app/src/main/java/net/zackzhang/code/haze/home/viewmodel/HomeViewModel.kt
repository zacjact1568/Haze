package net.zackzhang.code.haze.home.viewmodel

import net.zackzhang.code.haze.city.model.entity.CityWeatherEntity
import net.zackzhang.code.haze.common.constant.EVENT_CITY_CHANGED
import net.zackzhang.code.haze.common.constant.EVENT_DATA_LOADED
import net.zackzhang.code.haze.common.constant.EVENT_THEME_CHANGED
import net.zackzhang.code.haze.common.model.entity.ThemeEntity
import net.zackzhang.code.haze.common.viewmodel.Event
import net.zackzhang.code.haze.common.viewmodel.EventViewModel

class HomeViewModel : EventViewModel() {

    var cityName: String? = null
        private set

    var theme: ThemeEntity? = null
        private set

    fun notifyDataLoaded(city: CityWeatherEntity) {
        cityName = city.name
        eventLiveData.value = Event(EVENT_DATA_LOADED, city)
    }

    fun notifyCityChanged(city: CityWeatherEntity) {
        cityName = city.name
        eventLiveData.value = Event(EVENT_CITY_CHANGED, city)
    }

    fun notifyThemeChanged(theme: ThemeEntity) {
        this.theme = theme
        eventLiveData.value = Event(EVENT_THEME_CHANGED, theme)
    }
}