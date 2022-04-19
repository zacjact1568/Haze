package net.zackzhang.code.haze.city.viewmodel

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import net.zackzhang.code.haze.city.model.entity.CityWeatherEntity
import net.zackzhang.code.haze.common.constant.CITY
import net.zackzhang.code.haze.base.viewmodel.Event
import net.zackzhang.code.haze.base.viewmodel.BaseViewModel
import net.zackzhang.code.haze.common.constant.EVENT_ACTIVITY_FINISH
import net.zackzhang.code.haze.common.constant.RESULT_CODE_CITY_NEW

class CityViewModel : BaseViewModel() {

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
        eventLiveData.value = Event(EVENT_ACTIVITY_FINISH,
            ActivityResult(RESULT_CODE_CITY_NEW, if (city != null) {
                Intent().apply { putExtra(CITY, city) }
            } else null))
    }
}