package net.zackzhang.code.haze.weather.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.view.card.BaseCard
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.databinding.CardWeatherHourlyBinding
import net.zackzhang.code.haze.weather.viewmodel.data.WeatherHourlyCardData

class WeatherHourlyCard(parent: ViewGroup) : BaseCard(parent, R.layout.card_weather_hourly) {

    private val binding = CardWeatherHourlyBinding.bind(itemView)

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is WeatherHourlyCardData) return
        binding.run {
            vTime.text = cardData.time
            vConditionIcon.setImageResource(cardData.conditionIconRes)
            vTemperature.text = cardData.temperature
        }
    }
}