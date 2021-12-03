package net.zackzhang.code.haze.weather.view.card

import android.view.ViewGroup
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.view.InlineListAdapter
import net.zackzhang.code.haze.common.view.card.BaseCard
import net.zackzhang.code.haze.common.viewmodel.data.BaseCardData
import net.zackzhang.code.haze.databinding.CardWeatherDailyBinding
import net.zackzhang.code.haze.weather.viewmodel.data.WeatherDailyCardData

class WeatherDailyCard(parent: ViewGroup) : BaseCard(parent, R.layout.card_weather_daily) {

    private val binding = CardWeatherDailyBinding.bind(itemView)

    private val itemAdapter = InlineListAdapter { parent -> WeatherDailyItemCard(parent) }

    init {
        binding.root.adapter = itemAdapter
    }

    override fun updateViews(cardData: BaseCardData) {
        if (cardData !is WeatherDailyCardData) return
        itemAdapter.setCardData(cardData.daily)
    }
}