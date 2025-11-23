package net.zackzhang.code.haze.components.recycler.cards

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.components.recycler.CardAdapter
import net.zackzhang.code.haze.components.recycler.PaddingItemDecoration
import net.zackzhang.code.haze.utils.Orientation
import net.zackzhang.code.haze.utils.getDimension
import net.zackzhang.code.haze.databinding.CardWeatherHourlyRowBinding
import net.zackzhang.code.haze.utils.ItemDecorationRectInsets

class WeatherHourlyRowCard(parent: ViewGroup) : CardBase(parent, R.layout.card_weather_hourly_row) {

    private val binding = CardWeatherHourlyRowBinding.bind(itemView)

    private val cardAdapter = CardAdapter { type, parent ->
        when (type) {
            CardType.WEATHER_HOURLY.ordinal -> WeatherHourlyCard(parent)
            else -> null
        }
    }

    init {
        binding.vCardList.run {
            adapter = cardAdapter
            addItemDecoration(
                PaddingItemDecoration(
                    context.getDimension(R.dimen.dp_10),
                    0,
                    Orientation.HORIZONTAL
                )
            )
        }
    }

    override fun updateViews(config: CardConfigBase) {
        if (config !is WeatherHourlyRowCardConfig) return
        cardAdapter.setCardConfigs(config.hourly)
    }
}

data class WeatherHourlyRowCardConfig(
    val hourly: List<WeatherHourlyCardConfig>
) : CardConfigBase(
    CardType.WEATHER_HOURLY_ROW,
    decorationRectInsets = ItemDecorationRectInsets(
        left = false,
        right = false,
        top = true,
        bottom = true
    ),
)