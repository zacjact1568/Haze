package me.imzack.app.cold.model.preference

import android.content.SharedPreferences
import android.preference.PreferenceManager

import me.imzack.app.cold.App
import me.imzack.app.cold.R
import me.imzack.app.cold.common.Constant

class PreferenceHelper {

    private val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.context)

    var needGuideValue
        get() = mSharedPreferences.getBoolean(Constant.PREF_KEY_NEED_GUIDE, true)
        set(value) = mSharedPreferences.edit().putBoolean(Constant.PREF_KEY_NEED_GUIDE, value).apply()

    var nightModeValue
        get() = mSharedPreferences.getBoolean(Constant.PREF_KEY_NIGHT_MODE, false)
        set(value) = mSharedPreferences.edit().putBoolean(Constant.PREF_KEY_NIGHT_MODE, value).apply()

    var locationServiceValue
        get() = mSharedPreferences.getBoolean(Constant.PREF_KEY_LOCATION_SERVICE, false)
        set(value) = mSharedPreferences.edit().putBoolean(Constant.PREF_KEY_LOCATION_SERVICE, value).apply()

    val allValues: Map<String, *>
        get() = mSharedPreferences.all

    fun resetAllValues() {
        mSharedPreferences.edit().clear().putBoolean(Constant.PREF_KEY_NEED_GUIDE, false).apply()
        PreferenceManager.setDefaultValues(App.context, R.xml.preferences, true)
    }

    fun registerOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}
