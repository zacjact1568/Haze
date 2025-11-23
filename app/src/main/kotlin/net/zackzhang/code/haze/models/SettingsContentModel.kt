package net.zackzhang.code.haze.models

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.components.recycler.cards.CardType
import net.zackzhang.code.haze.components.recycler.cards.SettingsInfoPreferenceCardConfig
import net.zackzhang.code.haze.components.recycler.cards.SettingsPreferenceBasicCardConfig
import net.zackzhang.code.haze.components.recycler.cards.SettingsSwitchPreferenceCardConfig
import net.zackzhang.code.haze.models.sources.AppPreferenceManager
import net.zackzhang.code.haze.utils.getString
import net.zackzhang.code.util.log
import net.zackzhang.code.util.versionName

class SettingsContentViewModel : ModelBase() {

    private val cardConfigs = listOf(
        SettingsSwitchPreferenceCardConfig(
            "show_current_zone_time",
            R.string.preference_title_show_current_zone_time,
            R.string.preference_summary_show_current_zone_time_on,
            R.string.preference_summary_show_current_zone_time_off,
            R.drawable.ic_user_clock,
        ),
        SettingsInfoPreferenceCardConfig(
            "about",
            R.string.preference_title_about,
            getString(R.string.preference_summary_about_format, versionName),
        ),
    )

    private val cardLiveData by lazy {
        MutableLiveData<List<SettingsPreferenceBasicCardConfig>>()
    }

    override fun onThemeChanged() {
        cardConfigs.forEach {
            it.theme = theme
        }
        // 如果 cardLiveData 还未设置初值，表示 notifyLoadingData 还未执行完成，不通知 observer
        // 因为要保证 notifyLoadingData 是第一个通知（即设置初值）的
        if (cardLiveData.value != null) {
            cardLiveData.value = cardConfigs
        }
    }

    fun observeCard(owner: LifecycleOwner, observer: (List<SettingsPreferenceBasicCardConfig>) -> Unit) {
        cardLiveData.observe(owner, observer)
    }

    fun notifyLoadingData() {
        if (cardLiveData.value != null) return
        viewModelScope.launch {
            val prefValueList = AppPreferenceManager.get(cardConfigs.map {
                if (it.persistent) it.key else null
            })
            assert(cardConfigs.size == prefValueList.size)
            cardConfigs.zip(prefValueList).forEach {
                when (it.first.type) {
                    CardType.SETTINGS_SWITCH_PREFERENCE -> {
                        (it.first as SettingsSwitchPreferenceCardConfig).run {
                            checked = (if (it.second == null) default else it.second) as Boolean
                        }
                    }
                    CardType.SETTINGS_INFO_PREFERENCE -> assert(it.second == null)
                    else -> {}
                }
            }
            cardLiveData.value = cardConfigs
        }
    }

    fun notifyPreferenceClicked(position: Int) {
        val cd = cardConfigs[position]
        log(this::class, "notifyPreferenceClicked", cd.title)
    }

    fun notifyPreferenceUpdated(position: Int, value: Any) {
        val cd = cardConfigs[position]
        viewModelScope.launch {
            when (cd.type) {
                CardType.SETTINGS_SWITCH_PREFERENCE -> {
                    AppPreferenceManager.putBoolean(cd.key.name, value as Boolean)
                }
                else -> {}
            }
        }
    }
}