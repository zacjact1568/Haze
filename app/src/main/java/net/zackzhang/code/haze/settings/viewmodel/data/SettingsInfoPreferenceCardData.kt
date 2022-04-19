package net.zackzhang.code.haze.settings.viewmodel.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import net.zackzhang.code.haze.base.util.getString
import net.zackzhang.code.haze.common.constant.CARD_TYPE_SETTINGS_INFO_PREFERENCE

class SettingsInfoPreferenceCardData(
    key: Preferences.Key<Int>,
    title: String,
    summary: String,
    @DrawableRes iconResId: Int = 0,
) : SettingsPreferenceBaseCardData(
    key,
    title,
    summary,
    iconResId,
    false,
    type = CARD_TYPE_SETTINGS_INFO_PREFERENCE,
) {

    constructor(
        key: String,
        @StringRes titleResId: Int,
        summary: String,
        @DrawableRes iconResId: Int = 0,
    ) : this(
        intPreferencesKey(key),
        getString(titleResId),
        summary,
        iconResId,
    )
}