package me.imzack.app.cold.view.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v14.preference.SwitchPreference
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatDelegate
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import me.imzack.app.cold.R
import me.imzack.app.cold.common.Constant
import me.imzack.app.cold.model.DataManager
import me.imzack.app.cold.view.activity.HomeActivity
import me.imzack.app.cold.view.dialog.MessageDialogFragment

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {

        private const val TAG_SWITCH_NIGHT_MODE = "switch_night_mode"
    }

    private val locationServicePreference by lazy {
        findPreference(Constant.PREF_KEY_LOCATION_SERVICE) as SwitchPreference
    }
    private val nightModePreference by lazy {
        findPreference(Constant.PREF_KEY_NIGHT_MODE) as SwitchPreference
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        locationServicePreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ -> TODO() }

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

        if (childFragment.tag == TAG_SWITCH_NIGHT_MODE) {
            (childFragment as MessageDialogFragment).okButtonClickListener = { nightModePreference.isChecked = !nightModePreference.isChecked }
        }
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
                // 返回并重新创建 HomeActivity
                HomeActivity.start(context!!)
            }
        }
    }
}