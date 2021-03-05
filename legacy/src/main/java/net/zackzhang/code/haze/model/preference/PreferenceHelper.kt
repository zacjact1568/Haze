package net.zackzhang.code.haze.model.preference

import android.content.SharedPreferences
import android.preference.PreferenceManager

import net.zackzhang.code.haze.App
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.Constant

class PreferenceHelper {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.context)

    var needGuideValue
        get() = sharedPreferences.getBoolean(Constant.PREF_KEY_NEED_GUIDE, true)
        set(value) = sharedPreferences.edit().putBoolean(Constant.PREF_KEY_NEED_GUIDE, value).apply()

    var locationServiceValue
        get() = sharedPreferences.getBoolean(Constant.PREF_KEY_LOCATION_SERVICE, false)
        set(value) = sharedPreferences.edit().putBoolean(Constant.PREF_KEY_LOCATION_SERVICE, value).apply()

    var haveRequestedLocationPermissionsValue
        get() = sharedPreferences.getBoolean(Constant.PREF_KEY_HAVE_REQUESTED_LOCATION_PERMISSIONS, false)
        set(value) = sharedPreferences.edit().putBoolean(Constant.PREF_KEY_HAVE_REQUESTED_LOCATION_PERMISSIONS, value).apply()

    val allValues: Map<String, *>
        get() = sharedPreferences.all

    fun resetAllValues() {
        sharedPreferences.edit().clear().putBoolean(Constant.PREF_KEY_NEED_GUIDE, false).apply()
        PreferenceManager.setDefaultValues(App.context, R.xml.preferences, true)
    }

    fun registerOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}
