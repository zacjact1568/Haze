package net.zackzhang.code.haze.components.recycler.cards

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.databinding.CardWeatherTitleBinding

class WeatherTitleCard(parent: ViewGroup) : CardBase(parent, R.layout.card_weather_title) {

    private val binding = CardWeatherTitleBinding.bind(itemView)

    override fun updateViews(config: CardConfigBase) {
        if (config !is WeatherTitleCardConfig) return
        binding.root.text = config.title
    }
}

class WeatherTitleCardConfig(
    val title: String,
) : CardConfigBase(CardType.WEATHER_TITLE)