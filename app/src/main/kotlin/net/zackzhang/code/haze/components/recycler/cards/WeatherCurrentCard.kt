package net.zackzhang.code.haze.components.recycler.cards

import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.utils.orPlaceholder
import net.zackzhang.code.haze.utils.scale
import net.zackzhang.code.haze.utils.tintColor
import net.zackzhang.code.haze.databinding.CardWeatherCurrentBinding

class WeatherCurrentCard(parent: ViewGroup) : CardBase(parent, R.layout.card_weather_current) {

    private val binding = CardWeatherCurrentBinding.bind(itemView)

    override fun updateViews(config: CardConfigBase) {
        if (config !is WeatherCurrentCardConfig) return
        binding.run {
            vIcon.setImageResource(config.iconResId)
            vIcon.tintColor = config.iconColor
            vIcon.scale = config.iconScale
            vValue.text = config.value.orPlaceholder()
            vDescription.text = config.description
        }
    }
}

data class WeatherCurrentCardConfig(
    @param:DrawableRes val iconResId: Int,
    @param:ColorInt val iconColor: Int,
    val value: String?,
    val description: String,
    val iconScale: Float = 1F,
) : CardConfigBase(CardType.WEATHER_CURRENT, spanSize = 1)