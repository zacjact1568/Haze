package net.zackzhang.code.haze.city.viewmodel

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import net.zackzhang.code.haze.city.model.entity.CityWeatherEntity
import net.zackzhang.code.haze.common.Constants
import net.zackzhang.code.haze.common.viewmodel.Event
import net.zackzhang.code.haze.common.viewmodel.EventViewModel

class CityViewModel : EventViewModel() {

    private val searchInputLiveData by lazy {
        MutableLiveData<String>()
    }

    fun observeSearchInput(owner: LifecycleOwner, observer: (String) -> Unit) {
        searchInputLiveData.observe(owner, observer)
    }

    fun notifySearchInputChanged(input: String) {
        searchInputLiveData.value = input
    }

    fun notifyFinish(city: CityWeatherEntity?) {
        eventLiveData.value = Event(Constants.EVENT_ACTIVITY_FINISH,
            ActivityResult(Constants.RESULT_CODE_CITY_NEW, if (city != null) {
                Intent().apply { putExtra(Constants.CITY, city) }
            } else null))
    }
}