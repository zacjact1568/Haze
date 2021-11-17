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
            vTemperatureNow.updateTextOrPlaceholder(
                cardData.temperatureNow,
                cardData.theme.foregroundColor
            )
            vCondition.updateTextOrPlaceholder(
                cardData.condition,
                cardData.theme.foregroundColor
            )
            vAirQuality.updateTextOrPlaceholder(
                context.getString(R.string.air_quality_format, cardData.airQuality.orPlaceholder()),
                cardData.theme.foregroundColor
            )
            vTemperatureMin.updateTextOrPlaceholder(
                cardData.temperatureRange?.first,
                cardData.theme.foregroundColor
            )
            vTemperatureRangeBar.setData(
                cardData.temperatureRange,
                cardData.temperatureNow,
                cardData.theme.foregroundColor
            )
            vTemperatureMax.updateTextOrPlaceholder(
                cardData.temperatureRange?.last,
                cardData.theme.foregroundColor
            )
            vUpdatedAt.updateTextOrPlaceholder(
                context.getString(R.string.updated_at_format, cardData.updatedAt.orPlaceholder()),
                cardData.theme.foregroundColor
            )
        }
    }
}