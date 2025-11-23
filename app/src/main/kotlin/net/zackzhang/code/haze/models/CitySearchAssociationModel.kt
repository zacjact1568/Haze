package net.zackzhang.code.haze.models

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.zackzhang.code.haze.BuildConfig
import net.zackzhang.code.haze.components.recycler.cards.CitySearchAssociationCardConfig
import net.zackzhang.code.haze.models.sources.AppDatabaseInstance
import net.zackzhang.code.haze.models.sources.CityEntity
import net.zackzhang.code.haze.models.sources.CityWeatherEntity
import net.zackzhang.code.haze.models.sources.CityServices
import net.zackzhang.code.haze.utils.makeQWeatherSignature
import net.zackzhang.code.haze.utils.seconds
import net.zackzhang.code.util.log
import java.time.ZonedDateTime

class CitySearchAssociationViewModel : ModelBase() {

    val emptyInput get() = entityLiveData.value?.input.isNullOrEmpty()

    val emptyList get() = cardLiveData.value.isNullOrEmpty()

    private val entityLiveData by lazy {
        MutableLiveData<CitySearchEntity>()
    }

    private val cardLiveData by lazy {
        entityLiveData.map { it.result.toCardConfigs() }
    }

    private var searchJob: Job? = null

    fun observeCard(owner: LifecycleOwner, observer: (List<CitySearchAssociationCardConfig>) -> Unit) {
        cardLiveData.observe(owner, observer)
    }

    fun notifySearching(input: String) {
        // 如果正在请求，取消（处理快速输入）
        searchJob?.run {
            if (!isCompleted) {
                cancel(CancellationException("Last search has been cancelled because new input came"))
            }
        }
        // 在 recreate 的时候会被调用两次
        // 1. LiveData 订阅的时候
        // 2. EditText 输入的文本恢复的时候
        // 通过判断 input 是否与 CitySearchEntity#input 相同，来过滤掉这两种情况
        val entity = entityLiveData.value
        if (entity != null && entity.input == input) {
            // 即便数据没有变化，也需要按原值重新通知一下 observer
            // 否则 recreate 后无法正确设置 UI
            entityLiveData.value = entityLiveData.value
            return
        }
        if (input.isEmpty()) {
            entityLiveData.value = CitySearchEntity(input, emptyList())
            return
        }
        searchJob = viewModelScope.launch {
            runCatching {
                // 等待 0.5s 再请求（处理快速输入）
                delay(500)
                val time = seconds
                CityServices.getSearchAssociation(
                    input,
                    BuildConfig.QWEATHER_PUBLIC_ID,
                    time,
                    makeQWeatherSignature(input, time)
                )
            }.onSuccess {
                entityLiveData.value = CitySearchEntity(input, it)
            }.onFailure {
                when (it) {
                    // NetworkUtils.responseBodyToJsonObject
                    is IllegalArgumentException ->
                        entityLiveData.value = CitySearchEntity(input, emptyList())
                    is CancellationException -> log(
                        this@CitySearchAssociationViewModel::class,
                        "notifySearching",
                        it.toString(),
                    )
                }
            }
        }
    }

    fun notifySelected(position: Int) {
        val city = entityLiveData.value?.result?.get(position)
        if (city == null) {
            notifyEvent(CitySelectedEvent(null))
            return
        }
        viewModelScope.launch {
            city.createdAt = ZonedDateTime.now()
            AppDatabaseInstance.cityDao().replaceBy(city)
            notifyEvent(CitySelectedEvent(CityWeatherEntity(city.id, city.name)))
        }
    }

    private fun List<CityEntity>.toCardConfigs(): List<CitySearchAssociationCardConfig> {
        val cardConfigs = mutableListOf<CitySearchAssociationCardConfig>()
        forEach {
            cardConfigs += CitySearchAssociationCardConfig(
                it.name,
                it.prefecture,
                it.province,
                it.country,
            )
        }
        return cardConfigs
    }
}

private data class CitySearchEntity(
    val input: String,
    val result: List<CityEntity>,
)

data class CitySelectedEvent(val entity: CityWeatherEntity?) : Event