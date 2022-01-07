package net.zackzhang.code.haze.settings.view.card

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.view.card.BaseCard
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.databinding.CardSettingsPreferenceBaseBinding
import net.zackzhang.code.haze.settings.viewmodel.data.SettingsPreferenceBaseCardData

open class SettingsPreferenceBaseCard(
    parent: ViewGroup,
    @LayoutRes widgetResId: Int = 0,
    onClick: ((position: Int) -> Unit)? = null,
) : BaseCard(parent, R.layout.card_settings_preference_base, onClick) {

    protected val binding = CardSettingsPreferenceBaseBinding.bind(itemView)

    protected val widget = if (widgetResId == 0) null else {
        binding.vWidgetStub.run {
            layoutResource = widgetResId
            inflate()
        }
    }

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is SettingsPreferenceBaseCardData) return
        binding.run {
            vIcon.setImageResource(cardData.iconResId)
            vTitle.text = cardData.title
            vSummary.text = cardData.summary
        }
    }
}