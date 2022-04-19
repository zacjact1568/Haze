package net.zackzhang.code.haze.settings.viewmodel.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import net.zackzhang.code.haze.base.util.getString
import net.zackzhang.code.haze.common.constant.CARD_TYPE_SETTINGS_SWITCH_PREFERENCE

class SettingsSwitchPreferenceCardData(
    key: Preferences.Key<Boolean>,
    title: String,
    val summaryOn: String,
    val summaryOff: String,
    @DrawableRes iconResId: Int = 0,
    var checked: Boolean = false,
) : SettingsPreferenceBaseCardData(
    key,
    title,
    iconResId = iconResId,
    default = checked,
    type = CARD_TYPE_SETTINGS_SWITCH_PREFERENCE,
) {

    constructor(
        key: String,
        @StringRes titleResId: Int,
        @StringRes summaryOnResId: Int,
        @StringRes summaryOffResId: Int,
        @DrawableRes iconResId: Int = 0,
    ) : this(
        booleanPreferencesKey(key),
        getString(titleResId),
        getString(summaryOnResId),
        getString(summaryOffResId),
        iconResId,
    )
}