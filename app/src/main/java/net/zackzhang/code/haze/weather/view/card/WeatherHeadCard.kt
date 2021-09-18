package net.zackzhang.code.haze.weather.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.util.ViewUtils.orPlaceholder
import net.zackzhang.code.haze.common.util.ViewUtils.toStringOrPlaceholder
import net.zackzhang.code.haze.common.view.card.BaseCard
import net.zackzhang.code.haze.databinding.CardWeatherHeadBinding
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.weather.viewmodel.data.WeatherHeadCardData

class WeatherHeadCard(parent: ViewGroup) : BaseCard(parent, R.layout.card_weather_head) {

    private val binding = CardWeatherHeadBinding.bind(itemView)

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is WeatherHeadCardData) return
        binding.run {
            vTemperatureNow.text = cardData.temperatureNow.toStringOrPlaceholder()
            vCondition.text = cardData.condition.orPlaceholder()
            vAirQuality.text = cardData.airQuality.orPlaceholder()
            vTemperatureMin.text = cardData.temperatureRange?.first.toStringOrPlaceholder()
            vTemperatureRangeBar.setData(cardData.temperatureRange, cardData.temperatureNow)
            vTemperatureMax.text = cardData.temperatureRange?.last.toStringOrPlaceholder()
        }
    }
}