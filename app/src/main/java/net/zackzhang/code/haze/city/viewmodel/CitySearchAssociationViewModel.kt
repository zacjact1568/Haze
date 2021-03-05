package net.zackzhang.code.haze.city.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.zackzhang.code.haze.city.model.entity.CityEntity
import net.zackzhang.code.haze.city.model.entity.CitySearchEntity
import net.zackzhang.code.haze.city.model.entity.CityWeatherEntity
import net.zackzhang.code.haze.city.model.local.CityLocalRepository
import net.zackzhang.code.haze.city.model.remote.CityRemoteRepository
import net.zackzhang.code.haze.city.viewmodel.data.CitySearchAssociationCardData
import net.zackzhang.code.haze.common.Constants
import net.zackzhang.code.haze.common.exception.PlaceholderException
import net.zackzhang.code.haze.common.viewmodel.Event
import net.zackzhang.code.haze.common.viewmodel.EventViewModel

class CitySearchAssociationViewModel : EventViewModel() {

    private val entityLiveData by lazy {
        MutableLiveData<CitySearchEntity>()
    }

    private val cardLiveData by lazy {
        Transformations.map(entityLiveData) { it.result.toCardDataList() }
    }

    fun observeCard(owner: LifecycleOwner, observer: (List<CitySearchAssociationCardData>) -> Unit) {
        cardLiveData.observe(owner, observer)
    }

    fun notifySearching(input: String) {
        // 在 recreate 的时候会被调用两次
        // 1. LiveData 订阅的时候
        // 2. EditText 输入的文本恢复的时候
        // 通过判断 input 是否与 CitySearchEntity#input 相同，来过滤掉这两种情况
        val entity = entityLiveData.value
        if (entity != null && entity.input == input) return
        if (input.isEmpty()) {
            entityLiveData.value = CitySearchEntity(input, emptyList())
            return
        }
        viewModelScope.launch {
            runCatching {
                CityRemoteRepository.getCityList(input)
            }.onSuccess {
                entityLiveData.value = CitySearchEntity(input, it)
            }.onFailure {
                if ((it as? PlaceholderException)?.code == 404) {
                    entityLiveData.value = CitySearchEntity(input, emptyList())
                }
            }
        }
    }

    fun notifySelected(position: Int) {
        val city = entityLiveData.value?.result?.get(position)
        if (city == null) {
            eventLiveData.value = Event(Constants.EVENT_CITY_SELECTED, null)
            return
        }
        viewModelScope.launch {
            CityLocalRepository.replaceBy(city)
            eventLiveData.value = Event(Constants.EVENT_CITY_SELECTED, CityWeatherEntity(city.id, city.name))
        }
    }

    private fun List<CityEntity>.toCardDataList(): List<CitySearchAssociationCardData> {
        val cardDataList = mutableListOf<CitySearchAssociationCardData>()
        forEach {
            cardDataList += CitySearchAssociationCardData(
                it.name ?: Constants.PLACEHOLDER,
                it.prefecture ?: Constants.PLACEHOLDER,
                it.province ?: Constants.PLACEHOLDER
            )
        }
        return cardDataList
    }
}