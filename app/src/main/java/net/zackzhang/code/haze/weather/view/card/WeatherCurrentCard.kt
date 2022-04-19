package net.zackzhang.code.haze.weather.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.base.util.orPlaceholder
import net.zackzhang.code.haze.base.util.scale
import net.zackzhang.code.haze.base.util.tintColor
import net.zackzhang.code.haze.base.view.card.BaseCard
import net.zackzhang.code.haze.base.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.databinding.CardWeatherCurrentBinding
import net.zackzhang.code.haze.weather.viewmodel.data.WeatherCurrentCardData

class WeatherCurrentCard(parent: ViewGroup) : BaseCard(parent, R.layout.card_weather_current) {

    private val binding = CardWeatherCurrentBinding.bind(itemView)

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is WeatherCurrentCardData) return
        binding.run {
            vIcon.setImageResource(cardData.iconResId)
            vIcon.tintColor = cardData.iconColor
            vIcon.scale = cardData.iconScale
            vValue.text = cardData.value.orPlaceholder()
            vDescription.text = cardData.description
        }
    }
}