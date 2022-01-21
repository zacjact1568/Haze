package net.zackzhang.code.haze.weather.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.constant.CARD_TYPE_WEATHER_HOURLY
import net.zackzhang.code.haze.common.util.Orientation
import net.zackzhang.code.haze.common.util.getDimension
import net.zackzhang.code.haze.common.view.CardAdapter
import net.zackzhang.code.haze.common.view.card.BaseCard
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.databinding.CardWeatherHourlyRowBinding
import net.zackzhang.code.haze.weather.view.ui.PaddingItemDecoration
import net.zackzhang.code.haze.weather.viewmodel.data.WeatherHourlyRowCardData

class WeatherHourlyRowCard(parent: ViewGroup) : BaseCard(parent, R.layout.card_weather_hourly_row) {

    private val binding = CardWeatherHourlyRowBinding.bind(itemView)

    private val cardAdapter = CardAdapter { type, parent ->
        when (type) {
            CARD_TYPE_WEATHER_HOURLY -> WeatherHourlyCard(parent)
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

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is WeatherHourlyRowCardData) return
        cardAdapter.setCardData(cardData.hourly)
    }
}