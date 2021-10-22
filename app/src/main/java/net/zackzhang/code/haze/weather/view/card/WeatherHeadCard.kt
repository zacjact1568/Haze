package net.zackzhang.code.haze.weather.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.util.ViewUtils.orPlaceholder
import net.zackzhang.code.haze.common.util.ViewUtils.toStringOrPlaceholder
import net.zackzhang.code.haze.common.util.updateText
import net.zackzhang.code.haze.common.view.card.BaseCard
import net.zackzhang.code.haze.databinding.CardWeatherHeadBinding
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.weather.viewmodel.data.WeatherHeadCardData

class WeatherHeadCard(parent: ViewGroup) : BaseCard(parent, R.layout.card_weather_head) {

    private val binding = CardWeatherHeadBinding.bind(itemView)

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is WeatherHeadCardData) return
        binding.run {
            root.setBackgroundColor(cardData.theme.backgroundColor)
            vTemperatureNow.updateText(
                cardData.temperatureNow.toStringOrPlaceholder(),
                cardData.theme.foregroundColor
            )
            vCondition.updateText(
                cardData.condition.orPlaceholder(),
                cardData.theme.foregroundColor
            )
            vAirQuality.updateText(
                cardData.airQuality.orPlaceholder(),
                cardData.theme.foregroundColor
            )
            vTemperatureMin.updateText(
                cardData.temperatureRange?.first.toStringOrPlaceholder(),
                cardData.theme.foregroundColor
            )
            vTemperatureRangeBar.setData(
                cardData.temperatureRange,
                cardData.temperatureNow,
                cardData.theme.foregroundColor
            )
            vTemperatureMax.updateText(
                cardData.temperatureRange?.last.toStringOrPlaceholder(),
                cardData.theme.foregroundColor
            )
            vUpdatedAt.updateText(
                context.getString(R.string.updated_at_format, cardData.updatedAt.orPlaceholder()),
                cardData.theme.foregroundColor
            )
        }
    }
}