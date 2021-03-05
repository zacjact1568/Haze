package net.zackzhang.code.haze.weather.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.view.card.BaseCard
import net.zackzhang.code.haze.databinding.CardWeatherHeadBinding
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.weather.viewmodel.data.WeatherHeadCardData

class WeatherHeadCard(parent: ViewGroup) : BaseCard(parent, R.layout.card_weather_head) {

    private val binding = CardWeatherHeadBinding.bind(itemView)

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is WeatherHeadCardData) return
        binding.run {
            vTemperatureNow.text = cardData.temperatureNow
            vCondition.text = cardData.condition
            vAirQuality.text = cardData.airQuality
        }
    }
}