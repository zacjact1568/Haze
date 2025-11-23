package net.zackzhang.code.haze.components.recycler.cards

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.utils.orPlaceholder
import net.zackzhang.code.haze.utils.updateTextOrPlaceholder
import net.zackzhang.code.haze.databinding.CardWeatherHeadBinding
import net.zackzhang.code.haze.utils.ItemDecorationRectInsets

class WeatherHeadCard(parent: ViewGroup) : CardBase(parent, R.layout.card_weather_head) {

    private val binding = CardWeatherHeadBinding.bind(itemView)

    override fun updateViews(config: CardConfigBase) {
        if (config !is WeatherHeadCardConfig) return
        binding.run {
            vTemperatureNow.updateTextOrPlaceholder(config.temperatureNow)
            vCondition.updateTextOrPlaceholder(config.condition)
            vAirQuality.updateTextOrPlaceholder(
                context.getString(R.string.air_quality_format, config.airQuality.orPlaceholder()))
            vTemperatureMin.updateTextOrPlaceholder(config.temperatureRange?.first)
            vTemperatureRangeBar.setData(config.temperatureRange, config.temperatureNow)
            vTemperatureMax.updateTextOrPlaceholder(config.temperatureRange?.last)
            vUpdatedAt.updateTextOrPlaceholder(
                context.getString(R.string.updated_at_format, config.updatedAt.orPlaceholder()))
        }
    }
}

class WeatherHeadCardConfig(
    val temperatureNow: Int?,
    val temperatureRange: IntRange?,
    val condition: String?,
    val airQuality: String?,
    val updatedAt: String?,
) : CardConfigBase(
    CardType.WEATHER_HEAD,
    decorationRectInsets = ItemDecorationRectInsets(
        left = false,
        right = false,
        top = false,
        bottom = true
    ),
    needBackground = false,
)