package net.zackzhang.code.haze.view.fragment

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.v14.preference.SwitchPreference
import android.support.v4.app.Fragment
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import net.zackzhang.code.haze.App
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.Constant
import net.zackzhang.code.haze.event.CityAddedEvent
import net.zackzhang.code.haze.event.CityDeletedEvent
import net.zackzhang.code.haze.model.DataManager
import net.zackzhang.code.haze.model.bean.Weather
import net.zackzhang.code.haze.view.dialog.PreEnableLocationServiceDialogFragment

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val preferenceHelper = DataManager.preferenceHelper

    private val eventBus = App.eventBus

    private val locationServicePreference by lazy {
        findPreference(Constant.PREF_KEY_LOCATION_SERVICE) as SwitchPreference
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        locationServicePreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 该操作是开启位置服务，且为 Android 6.0 及以上
                // 先不执行更新 shared preference，待用户确认后更新
                // 界面不会更新
                PreEnableLocationServiceDialogFragment().show(childFragmentManager)
                // 返回 false 表示不改变 preference 的值
                false
            } else {
                // 该操作是关闭位置服务，或者低于 Android 6.0
                // 直接执行更新 shared preference，后续再在 onSharedPreferenceChanged 中判断是哪种
                // 界面会更新
                true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        when (childFragment.tag) {
            PreEnableLocationServiceDialogFragment.TAG_PRE_ENABLE_LOCATION_SERVICE ->
                (childFragment as PreEnableLocationServiceDialogFragment).okButtonClickListener = {
                    // 将位置服务设置项开启，触发 onSharedPreferenceChanged 回调，界面会更新
                    locationServicePreference.isChecked = true
                }
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
                    DataManager.notifyAddingCity(Weather(Constant.CITY_ID_CURRENT_LOCATION, getString(R.string.text_current_location), isLocationCity = true))
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
                    DataManager.notifyDeletingCity(0)
                    eventBus.post(CityDeletedEvent(
                            eventSource,
                            cityId,
                            0
                    ))
                }
            }
        }
    }
}