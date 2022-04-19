package net.zackzhang.code.haze.weather.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.base.view.card.BaseCard
import net.zackzhang.code.haze.base.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.databinding.CardWeatherTitleBinding
import net.zackzhang.code.haze.weather.viewmodel.data.WeatherTitleCardData

class WeatherTitleCard(parent: ViewGroup) : BaseCard(parent, R.layout.card_weather_title) {

    private val binding = CardWeatherTitleBinding.bind(itemView)

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is WeatherTitleCardData) return
        binding.root.text = cardData.title
    }
}