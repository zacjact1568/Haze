package net.zackzhang.app.cold.view.fragment

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.v14.preference.SwitchPreference
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatDelegate
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import net.zackzhang.app.cold.App
import net.zackzhang.app.cold.R
import net.zackzhang.app.cold.common.Constant
import net.zackzhang.app.cold.event.CityAddedEvent
import net.zackzhang.app.cold.event.CityDeletedEvent
import net.zackzhang.app.cold.model.DataManager
import net.zackzhang.app.cold.model.bean.Weather
import net.zackzhang.app.cold.view.activity.HomeActivity
import net.zackzhang.app.cold.view.dialog.MessageDialogFragment

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {

        private const val TAG_PRE_ENABLE_LOCATION_SERVICE = "pre_enable_location_service"
        private const val TAG_SWITCH_NIGHT_MODE = "switch_night_mode"
    }

    private val preferenceHelper = DataManager.preferenceHelper

    private val eventBus = App.eventBus

    private val locationServicePreference by lazy {
        findPreference(Constant.PREF_KEY_LOCATION_SERVICE) as SwitchPreference
    }
    private val nightModePreference by lazy {
        findPreference(Constant.PREF_KEY_NIGHT_MODE) as SwitchPreference
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        locationServicePreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 该操作是开启位置服务，且为 Android 6.0 及以上
                // 先不执行更新 shared preference，待用户确认后更新
                // 界面不会更新
                MessageDialogFragment.Builder()
                        .setTitle(R.string.title_dialog_pre_enable_location_service)
                        .setMessage(R.string.msg_dialog_pre_enable_location_service)
                        .setOkButtonText(R.string.pos_btn_dialog_pre_enable_location_service)
                        .showCancelButton()
                        .show(childFragmentManager, TAG_PRE_ENABLE_LOCATION_SERVICE)
                false
            } else {
                // 该操作是关闭位置服务，或者低于 Android 6.0
                // 直接执行更新 shared preference，后续再在 onSharedPreferenceChanged 中判断是哪种
                // 界面会更新
                true
            }
        }

        nightModePreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
            MessageDialogFragment.Builder()
                    .setTitle(R.string.title_dialog_switch_night_mode)
                    .setMessage(R.string.msg_dialog_switch_night_mode)
                    .setOkButtonText(R.string.button_restart)
                    .showCancelButton()
                    .show(childFragmentManager, TAG_SWITCH_NIGHT_MODE)
            // 返回 false 表示不改变 preference 的值
            false
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        when (childFragment.tag) {
            TAG_PRE_ENABLE_LOCATION_SERVICE -> (childFragment as MessageDialogFragment).okButtonClickListener = {
                // 将位置服务设置项开启，触发 onSharedPreferenceChanged 回调，界面会更新
                locationServicePreference.isChecked = true
            }
            TAG_SWITCH_NIGHT_MODE -> (childFragment as MessageDialogFragment).okButtonClickListener = { nightModePreference.isChecked = !nightModePreference.isChecked }
        }
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            Constant.PREF_KEY_LOCATION_SERVICE -> {
                val eventSource = javaClass.simpleName
                if (preferenceHelper.locationServiceValue) {
                    // 如果触发此回调的操作是开启位置服务
                    // 添加第一页“当前位置”城市页
                    DataManager.notifyCityAdded(Weather(Constant.CITY_ID_CURRENT_LOCATION, getString(R.string.text_current_location), isLocationCity = true))
                    eventBus.post(CityAddedEvent(
                            eventSource,
                            Constant.CITY_ID_CURRENT_LOCATION,
                            0
                    ))
                } else {
                    // 如果触发此回调的操作是关闭位置服务
                    // 移除第一页“当前位置”城市页
                    // 先保存 cityId，供发送事件使用，稍后删除后就获取不到了
                    val cityId = DataManager.getWeather(0).cityId
                    DataManager.notifyCityDeleted(0)
                    eventBus.post(CityDeletedEvent(
                            eventSource,
                            cityId,
                            0
                    ))
                }
            }
            Constant.PREF_KEY_NIGHT_MODE -> {
                AppCompatDelegate.setDefaultNightMode(if (preferenceHelper.nightModeValue) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
                // 返回并重新创建 HomeActivity
                HomeActivity.start(context!!)
            }
        }
    }
}