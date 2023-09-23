package net.zackzhang.code.haze.settings.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.databinding.WidgetSettingsSwitchPreferenceBinding
import net.zackzhang.code.haze.settings.viewmodel.data.SettingsSwitchPreferenceCardData

class SettingsSwitchPreferenceCard(
    parent: ViewGroup,
    onClick: (position: Int, checked: Boolean) -> Unit,
) : SettingsPreferenceBaseCard(parent, R.layout.widget_settings_switch_preference) {

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

    override fun updateViews(cardData: BaseCardData) {
        super.updateViews(cardData)
        if (cardData !is SettingsSwitchPreferenceCardData) return
        binding.vSummary.text = if (cardData.checked) cardData.summaryOn else cardData.summaryOff
        widgetBinding.root.run {
            isChecked = cardData.checked
            cardData.theme?.let {
                checkedColor = it.backgroundColor
            }
        }
    }
}