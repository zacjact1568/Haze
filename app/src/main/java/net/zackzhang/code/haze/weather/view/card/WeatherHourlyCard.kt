package net.zackzhang.code.haze.weather.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.constant.CARD_TYPE_WEATHER_HOURLY_ITEM
import net.zackzhang.code.haze.common.util.getDimension
import net.zackzhang.code.haze.common.view.CardAdapter
import net.zackzhang.code.haze.common.view.card.BaseCard
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.databinding.CardWeatherHourlyBinding
import net.zackzhang.code.haze.weather.view.ui.PaddingItemDecoration
import net.zackzhang.code.haze.weather.viewmodel.data.WeatherHourlyCardData

class WeatherHourlyCard(parent: ViewGroup) : BaseCard(parent, R.layout.card_weather_hourly) {

    private val binding = CardWeatherHourlyBinding.bind(itemView)

    private val cardAdapter = CardAdapter { type, parent ->
        when (type) {
            CARD_TYPE_WEATHER_HOURLY_ITEM -> WeatherHourlyItemCard(parent)
            else -> null
        }
    }

    init {
        binding.root.run {
            adapter = cardAdapter
            addItemDecoration(
                PaddingItemDecoration(
                    context.getDimension(R.dimen.dp_10),
                    context.getDimension(R.dimen.dp_12),
                    0
                )
            )
        }
    }

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is WeatherHourlyCardData) return
        cardAdapter.setCardData(cardData.hourly)
    }
}