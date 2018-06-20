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

        locationServicePreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
            // 在触摸 Location Service Preference 时才检查 LocationServicePermissionsFragment 是否已创建过
            // 若 Activity 从重建中恢复，且以前创建过 LocationServicePermissionsFragment，则 lspFragment 一定不为空
            var lspFragment = childFragmentManager.findFragmentByTag(LocationServicePermissionsFragment.TAG_LOCATION_SERVICE_PERMISSIONS) as LocationServicePermissionsFragment?
            if (lspFragment == null) {
                // 如果没有创建过 LocationServicePermissionsFragment，创建
                lspFragment = LocationServicePermissionsFragment()
                // 使用 commitNow 来立即执行，否则后面可能会找不到
                childFragmentManager.beginTransaction().add(lspFragment, LocationServicePermissionsFragment.TAG_LOCATION_SERVICE_PERMISSIONS).commitNow()
            }
            if (newValue as Boolean) {
                // 只有当开启位置服务时才执行
                lspFragment.requestPermissions()
                false
            } else {
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
            TAG_SWITCH_NIGHT_MODE -> (childFragment as MessageDialogFragment).okButtonClickListener = { nightModePreference.isChecked = !nightModePreference.isChecked }
            LocationServicePermissionsFragment.TAG_LOCATION_SERVICE_PERMISSIONS ->
                (childFragment as LocationServicePermissionsFragment).permissionsRequestFinishedListener = {
                    if (it) {
                        // 如果成功授权，更改 preference 的显示状态为 true，这也会同时更改 Shared Preference 中的值
                        locationServicePreference.isChecked = true
                    }
                    // 如果授权被拒绝，不做任何操作
                }
        }
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            Constant.PREF_KEY_LOCATION_SERVICE -> { }
            Constant.PREF_KEY_NIGHT_MODE -> {
                AppCompatDelegate.setDefaultNightMode(if (DataManager.preferenceHelper.nightModeValue) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
                // 返回并重新创建 HomeActivity
                HomeActivity.start(context!!)
            }
        }
    }
}