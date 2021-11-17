package net.zackzhang.code.haze.weather.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.view.card.BaseCard
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.databinding.CardWeatherHourlyItemBinding
import net.zackzhang.code.haze.weather.viewmodel.data.WeatherHourlyItemCardData

class WeatherHourlyItemCard(parent: ViewGroup) : BaseCard(parent, R.layout.card_weather_hourly_item) {

    private val binding = CardWeatherHourlyItemBinding.bind(itemView)

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is WeatherHourlyItemCardData) return
        binding.run {
            vTime.text = cardData.time
            vConditionIcon.setImageResource(cardData.conditionIconRes)
            vTemperature.text = cardData.temperature
        }
    }
}