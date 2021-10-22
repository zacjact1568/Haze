package net.zackzhang.code.haze.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import net.zackzhang.code.haze.city.model.entity.CityWeatherEntity
import net.zackzhang.code.haze.common.Constants
import net.zackzhang.code.haze.common.model.entity.ThemeEntity
import net.zackzhang.code.haze.common.view.CardAdapter
import net.zackzhang.code.haze.databinding.FragmentWeatherBinding
import net.zackzhang.code.haze.home.viewmodel.HomeViewModel
import net.zackzhang.code.haze.weather.view.card.WeatherHeadCard
import net.zackzhang.code.haze.weather.viewmodel.WeatherViewModel

class WeatherFragment : Fragment() {

    private val viewModel by viewModels<WeatherViewModel>()

    private val activityViewModel by activityViewModels<HomeViewModel>()

    private val cardAdapter = CardAdapter { type, parent ->
        when (type) {
            Constants.CARD_TYPE_WEATHER_HEAD -> WeatherHeadCard(parent)
            // Other cards
            else -> null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWeatherBinding.inflate(inflater, container, false)
        binding.root.setOnRefreshListener {
            viewModel.notifyRefreshing()
        }
        binding.vCardList.run {
            (layoutManager as? GridLayoutManager)?.getSpanSizeByColumn {
                cardAdapter.getColumn(it)
            }
            adapter = cardAdapter
        }

        viewModel.observeCard(viewLifecycleOwner) {
            binding.root.isRefreshing = false
            cardAdapter.setCardData(it)
        }
        viewModel.observeEvent(viewLifecycleOwner) {
            when (it.name) {
                Constants.EVENT_DATA_LOADED -> activityViewModel.notifyDataLoaded(it.data as CityWeatherEntity)
                Constants.EVENT_THEME_CHANGED -> activityViewModel.notifyThemeChanged(it.data as ThemeEntity)
            }
        }
        activityViewModel.observeEvent(viewLifecycleOwner) {
            when (it.name) {
                Constants.EVENT_CITY_CHANGED -> {
                    cardAdapter.setCardData(emptyList())
                    viewModel.notifyRefreshing((it.data as CityWeatherEntity).id)
                }
            }
        }
        viewModel.notifyLoadingData()

        return binding.root
    }

    private inline fun GridLayoutManager.getSpanSizeByColumn(crossinline getColumn: (Int) -> Int) {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = spanCount / getColumn(position)
        }
    }
}