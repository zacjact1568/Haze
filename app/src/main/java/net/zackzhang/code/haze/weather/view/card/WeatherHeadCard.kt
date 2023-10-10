package net.zackzhang.code.haze.weather.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.util.orPlaceholder
import net.zackzhang.code.haze.common.util.updateTextOrPlaceholder
import net.zackzhang.code.haze.common.view.card.BaseCard
import net.zackzhang.code.haze.databinding.CardWeatherHeadBinding
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.weather.viewmodel.data.WeatherHeadCardData

class WeatherHeadCard(parent: ViewGroup) : BaseCard(parent, R.layout.card_weather_head) {

    private val binding = CardWeatherHeadBinding.bind(itemView)

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is WeatherHeadCardData) return
        binding.run {
            vTemperatureNow.updateTextOrPlaceholder(cardData.temperatureNow)
            vCondition.updateTextOrPlaceholder(cardData.condition)
            vAirQuality.updateTextOrPlaceholder(
                context.getString(R.string.air_quality_format, cardData.airQuality.orPlaceholder()))
            vTemperatureMin.updateTextOrPlaceholder(cardData.temperatureRange?.first)
            vTemperatureRangeBar.setData(cardData.temperatureRange, cardData.temperatureNow)
            vTemperatureMax.updateTextOrPlaceholder(cardData.temperatureRange?.last)
            vUpdatedAt.updateTextOrPlaceholder(
                context.getString(R.string.updated_at_format, cardData.updatedAt.orPlaceholder()))
        }
    }
}