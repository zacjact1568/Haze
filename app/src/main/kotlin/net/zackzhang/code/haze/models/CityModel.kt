package net.zackzhang.code.haze.models

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import net.zackzhang.code.haze.definitions.Extra
import net.zackzhang.code.haze.definitions.RESULT_CODE_CITY_NEW
import net.zackzhang.code.haze.models.sources.CityWeatherEntity

class CityViewModel : ModelBase() {

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
        notifyEvent(
            Event.ActivityFinish(
                ActivityResult(
                    RESULT_CODE_CITY_NEW,
                    if (city != null) {
                        Intent().putExtra(Extra.CITY.name, city)
                    } else null,
                )
            )
        )
    }
}