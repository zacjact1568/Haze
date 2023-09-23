package net.zackzhang.code.haze.weather.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.util.toStringOrPlaceholder
import net.zackzhang.code.haze.common.view.card.BaseCard
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.databinding.CardWeatherDailyBinding
import net.zackzhang.code.haze.weather.viewmodel.data.WeatherDailyCardData

class WeatherDailyCard(parent: ViewGroup) : BaseCard(parent, R.layout.card_weather_daily) {

    private val binding = CardWeatherDailyBinding.bind(itemView)

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is WeatherDailyCardData) return
        binding.run {
            vDate.text = cardData.date
            vConditionIcon.setImageResource(cardData.conditionIconRes)
            vTemperatureMin.text = cardData.temperatureRange?.first.toStringOrPlaceholder()
            vTemperatureRangeBar.setData(
                cardData.temperatureRangeAmongAllDates,
                cardData.temperatureNow,
                cardData.temperatureRange,
            )
            vTemperatureMax.text = cardData.temperatureRange?.last.toStringOrPlaceholder()
        }
    }
}