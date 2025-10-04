package net.zackzhang.code.haze.settings.viewmodel.data

import androidx.annotation.DrawableRes
import androidx.datastore.preferences.core.Preferences
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData

abstract class SettingsPreferenceBaseCardData(
    val key: Preferences.Key<out Any>,
    val title: String,
    var summary: String? = null,
    @param:DrawableRes
    val iconResId: Int = 0,
    val persistent: Boolean = true,
    val default: Any? = null,
    type: Int,
) : BaseCardData(type)