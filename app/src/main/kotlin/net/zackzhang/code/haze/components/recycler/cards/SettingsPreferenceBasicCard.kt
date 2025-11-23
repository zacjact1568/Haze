package net.zackzhang.code.haze.components.recycler.cards

import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.databinding.CardSettingsPreferenceBasicBinding
import net.zackzhang.code.haze.utils.getString

open class SettingsPreferenceBasicCard(
    parent: ViewGroup,
    @LayoutRes widgetResId: Int = 0,
    onClick: ((position: Int) -> Unit)? = null,
) : CardBase(parent, R.layout.card_settings_preference_basic, onClick) {

    protected val binding = CardSettingsPreferenceBasicBinding.bind(itemView)

    protected val widget = if (widgetResId == 0) null else {
        binding.vWidgetStub.run {
            layoutResource = widgetResId
            inflate()
        }
    }

    override fun updateViews(config: CardConfigBase) {
        if (config !is SettingsPreferenceBasicCardConfig) return
        binding.run {
            vIcon.setImageResource(config.iconResId)
            vTitle.text = config.title
            vSummary.text = config.summary
        }
    }
}

open class SettingsPreferenceBasicCardConfig(
    val key: Preferences.Key<out Any>,
    val title: String,
    var summary: String? = null,
    @param:DrawableRes
    val iconResId: Int = 0,
    val persistent: Boolean = true,
    val default: Any? = null,
    type: CardType,
) : CardConfigBase(type)

class SettingsInfoPreferenceCardConfig(
    key: Preferences.Key<Int>,
    title: String,
    summary: String,
    @DrawableRes iconResId: Int = 0,
) : SettingsPreferenceBasicCardConfig(
    key,
    title,
    summary,
    iconResId,
    false,
    type = CardType.SETTINGS_INFO_PREFERENCE,
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