package net.zackzhang.code.haze.components.recycler.cards

import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.databinding.WidgetSettingsSwitchPreferenceBinding
import net.zackzhang.code.haze.utils.getString

class SettingsSwitchPreferenceCard(
    parent: ViewGroup,
    onClick: (position: Int, checked: Boolean) -> Unit,
) : SettingsPreferenceBasicCard(
    parent,
    R.layout.widget_settings_switch_preference,
) {

    private val widgetBinding = WidgetSettingsSwitchPreferenceBinding.bind(widget!!)

    init {
        binding.root.setOnClickListener {
            widgetBinding.root.performClick()
            onClick(layoutPosition, widgetBinding.root.isChecked)
        }
        widgetBinding.root.setOnCheckedChangeListener { _, isChecked ->
            onClick(layoutPosition, isChecked)
        }
    }

    override fun updateViews(config: CardConfigBase) {
        super.updateViews(config)
        if (config !is SettingsSwitchPreferenceCardConfig) return
        binding.vSummary.text = if (config.checked) config.summaryOn else config.summaryOff
        widgetBinding.root.run {
            isChecked = config.checked
            config.theme?.let {
                checkedColor = context.getColor(it.accentColor)
            }
        }
    }
}

class SettingsSwitchPreferenceCardConfig(
    key: Preferences.Key<Boolean>,
    title: String,
    val summaryOn: String,
    val summaryOff: String,
    @DrawableRes iconResId: Int = 0,
    var checked: Boolean = false,
) : SettingsPreferenceBasicCardConfig(
    key,
    title,
    iconResId = iconResId,
    default = checked,
    type = CardType.SETTINGS_SWITCH_PREFERENCE,
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