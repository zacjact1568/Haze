package net.zackzhang.code.haze.components.recycler.cards

import android.view.ViewGroup
import androidx.annotation.DrawableRes
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.utils.toStringOrPlaceholder
import net.zackzhang.code.haze.databinding.CardWeatherDailyBinding

class WeatherDailyCard(parent: ViewGroup) : CardBase(parent, R.layout.card_weather_daily) {

    private val binding = CardWeatherDailyBinding.bind(itemView)

    override fun updateViews(config: CardConfigBase) {
        if (config !is WeatherDailyCardConfig) return
        binding.run {
            vDate.text = config.date
            vConditionIcon.setImageResource(config.conditionIconRes)
            vTemperatureMin.text = config.temperatureRange?.first.toStringOrPlaceholder()
            vTemperatureRangeBar.setData(
                config.temperatureRangeAmongAllDates,
                config.temperatureNow,
                config.temperatureRange,
            )
            vTemperatureMax.text = config.temperatureRange?.last.toStringOrPlaceholder()
        }
    }
}

class WeatherDailyCardConfig(
    val date: String,
    @param:DrawableRes
    val conditionIconRes: Int,
    val temperatureNow: Int?,
    val temperatureRange: IntRange?,
    val temperatureRangeAmongAllDates: IntRange?,
) : CardConfigBase(CardType.WEATHER_DAILY)