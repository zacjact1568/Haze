package net.zackzhang.code.haze.weather.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.util.toStringOrPlaceholder
import net.zackzhang.code.haze.common.view.card.InlineBaseCard
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.databinding.CardWeatherDailyItemBinding
import net.zackzhang.code.haze.weather.viewmodel.data.WeatherDailyItemCardData

class WeatherDailyItemCard(parent: ViewGroup) : InlineBaseCard(parent, R.layout.card_weather_daily_item) {

    private val binding = CardWeatherDailyItemBinding.bind(itemView)

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is WeatherDailyItemCardData) return
        binding.run {
            vDate.text = cardData.date
            vConditionIcon.setImageResource(cardData.conditionIconRes)
            vTemperatureMin.text = cardData.temperatureRange?.first.toStringOrPlaceholder()
            vTemperatureMax.text = cardData.temperatureRange?.last.toStringOrPlaceholder()
        }
    }
}