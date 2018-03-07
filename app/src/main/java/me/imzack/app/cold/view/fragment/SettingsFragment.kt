package me.imzack.app.cold.view.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatDelegate
import me.imzack.app.cold.R
import me.imzack.app.cold.common.Constant
import me.imzack.app.cold.model.DataManager
import me.imzack.app.cold.view.activity.HomeActivity
import me.imzack.app.cold.view.dialog.MessageDialogFragment

class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val mLocationServicePreference by lazy {
        findPreference(Constant.PREF_KEY_LOCATION_SERVICE) as SwitchPreference
    }
    private val mNightModePreference by lazy {
        findPreference(Constant.PREF_KEY_NIGHT_MODE) as SwitchPreference
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)

        mLocationServicePreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ -> TODO() }

        mNightModePreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
            //这里需要使用宿主activity的support包中的FragmentManager
            MessageDialogFragment.newInstance(
                    getString(R.string.msg_dialog_switch_night_mode),
                    getString(R.string.title_dialog_switch_night_mode),
                    getString(R.string.button_restart),
                    { mNightModePreference.isChecked = !mNightModePreference.isChecked }
            ).show((activity as FragmentActivity).supportFragmentManager)
            //返回false表示不改变preference的值
            false
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            Constant.PREF_KEY_LOCATION_SERVICE -> { TODO() }
            Constant.PREF_KEY_NIGHT_MODE -> {
                AppCompatDelegate.setDefaultNightMode(if (DataManager.preferenceHelper.nightModeValue) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
                //返回并重新创建HomeActivity
                HomeActivity.start(activity)
            }
        }
    }
}