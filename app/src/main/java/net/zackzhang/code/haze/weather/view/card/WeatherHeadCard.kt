package net.zackzhang.code.haze.weather.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.base.util.orPlaceholder
import net.zackzhang.code.haze.base.util.updateTextOrPlaceholder
import net.zackzhang.code.haze.base.view.card.BaseCard
import net.zackzhang.code.haze.databinding.CardWeatherHeadBinding
import net.zackzhang.code.haze.base.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.weather.viewmodel.data.WeatherHeadCardData

class WeatherHeadCard(parent: ViewGroup) : BaseCard(parent, R.layout.card_weather_head) {

    private val binding = CardWeatherHeadBinding.bind(itemView)

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is WeatherHeadCardData) return
        binding.run {
            val theme = cardData.theme!!
            vTemperatureNow.updateTextOrPlaceholder(
                cardData.temperatureNow,
                theme.foregroundColor
            )
            vCondition.updateTextOrPlaceholder(
                cardData.condition,
                theme.foregroundColor
            )
            vAirQuality.updateTextOrPlaceholder(
                context.getString(R.string.air_quality_format, cardData.airQuality.orPlaceholder()),
                theme.foregroundColor
            )
            vTemperatureMin.updateTextOrPlaceholder(
                cardData.temperatureRange?.first,
                theme.foregroundColor
            )
            vTemperatureRangeBar.setData(
                cardData.temperatureRange,
                cardData.temperatureNow,
                theme.foregroundColor
            )
            vTemperatureMax.updateTextOrPlaceholder(
                cardData.temperatureRange?.last,
                theme.foregroundColor
            )
            vUpdatedAt.updateTextOrPlaceholder(
                context.getString(R.string.updated_at_format, cardData.updatedAt.orPlaceholder()),
                theme.foregroundColor
            )
        }
    }
}