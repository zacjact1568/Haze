package net.zackzhang.code.haze.components.recycler.cards

import android.view.ViewGroup
import androidx.annotation.DrawableRes
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.databinding.CardWeatherHourlyBinding

class WeatherHourlyCard(parent: ViewGroup) : CardBase(parent, R.layout.card_weather_hourly) {

    private val binding = CardWeatherHourlyBinding.bind(itemView)

    override fun updateViews(config: CardConfigBase) {
        if (config !is WeatherHourlyCardConfig) return
        binding.run {
            vTime.text = config.time
            vConditionIcon.setImageResource(config.conditionIconRes)
            vTemperature.text = config.temperature
        }
    }
}

data class WeatherHourlyCardConfig(
    val time: String,
    @param:DrawableRes
    val conditionIconRes: Int,
    val temperature: String,
) : CardConfigBase(CardType.WEATHER_HOURLY)