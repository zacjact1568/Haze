package net.zackzhang.code.haze.settings.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.zackzhang.code.haze.BuildConfig
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.constant.CARD_TYPE_SETTINGS_INFO_PREFERENCE
import net.zackzhang.code.haze.common.constant.CARD_TYPE_SETTINGS_SWITCH_PREFERENCE
import net.zackzhang.code.haze.common.constant.PREFERENCE_KEY_ABOUT
import net.zackzhang.code.haze.common.constant.PREFERENCE_KEY_SHOW_CURRENT_ZONE_TIME
import net.zackzhang.code.haze.common.model.entity.ThemeEntity
import net.zackzhang.code.haze.common.util.dLog
import net.zackzhang.code.haze.common.util.getString
import net.zackzhang.code.haze.common.viewmodel.BaseViewModel
import net.zackzhang.code.haze.settings.model.local.SettingsLocalRepository
import net.zackzhang.code.haze.settings.viewmodel.data.SettingsInfoPreferenceCardData
import net.zackzhang.code.haze.settings.viewmodel.data.SettingsPreferenceBaseCardData
import net.zackzhang.code.haze.settings.viewmodel.data.SettingsSwitchPreferenceCardData

class SettingsContentViewModel : BaseViewModel() {

    private val settingsCardDataList = listOf(
        SettingsSwitchPreferenceCardData(
            PREFERENCE_KEY_SHOW_CURRENT_ZONE_TIME,
            R.string.preference_title_show_current_zone_time,
            R.string.preference_summary_show_current_zone_time_on,
            R.string.preference_summary_show_current_zone_time_off,
            R.drawable.ic_user_clock,
        ),
        SettingsInfoPreferenceCardData(
            PREFERENCE_KEY_ABOUT,
            R.string.preference_title_about,
            getString(R.string.preference_summary_about_format, BuildConfig.VERSION_NAME),
        ),
    )

    private val cardLiveData by lazy {
        MutableLiveData<List<SettingsPreferenceBaseCardData>>()
    }

    fun observeCard(owner: LifecycleOwner, observer: (List<SettingsPreferenceBaseCardData>) -> Unit) {
        cardLiveData.observe(owner, observer)
    }

    fun notifyLoadingData() {
        if (cardLiveData.value != null) return
        viewModelScope.launch {
            val prefValueList = SettingsLocalRepository.getPreferences(settingsCardDataList.map {
                if (it.persistent) it.key else null
            })
            assert(settingsCardDataList.size == prefValueList.size)
            settingsCardDataList.zip(prefValueList).forEach {
                when (it.first.type) {
                    CARD_TYPE_SETTINGS_SWITCH_PREFERENCE -> {
                        (it.first as SettingsSwitchPreferenceCardData).run {
                            checked = (if (it.second == null) default else it.second) as Boolean
                        }
                    }
                    CARD_TYPE_SETTINGS_INFO_PREFERENCE -> assert(it.second == null)
                }
            }
            cardLiveData.value = settingsCardDataList
        }
    }

    fun notifyPreferenceClicked(position: Int) {
        val cd = settingsCardDataList[position]
        dLog(cd.title)
    }

    fun notifyPreferenceUpdated(position: Int, value: Any) {
        val cd = settingsCardDataList[position]
        viewModelScope.launch {
            when (cd.type) {
                CARD_TYPE_SETTINGS_SWITCH_PREFERENCE -> {
                    SettingsLocalRepository.putBooleanPreference(cd.key.name, value as Boolean)
                }
            }
        }
    }

    fun notifyThemeChanged(theme: ThemeEntity) {
        settingsCardDataList.forEach {
            it.theme = theme
        }
        // 如果 cardLiveData 还未设置初值，表示 notifyLoadingData 还未执行完成，不通知 observer
        // 因为要保证 notifyLoadingData 是第一个通知（即设置初值）的
        if (cardLiveData.value != null) {
            cardLiveData.value = settingsCardDataList
        }
    }
}