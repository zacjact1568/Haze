package net.zackzhang.code.haze.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.core.city.model.entity.CityWeatherEntity
import net.zackzhang.code.haze.base.view.ThemeEntity
import net.zackzhang.code.haze.base.util.getDimension
import net.zackzhang.code.haze.common.view.CardAdapter
import net.zackzhang.code.haze.base.view.SystemBarInsets
import net.zackzhang.code.haze.common.constant.*
import net.zackzhang.code.haze.common.viewmodel.data.SpaceCardData
import net.zackzhang.code.haze.databinding.FragmentWeatherBinding
import net.zackzhang.code.haze.home.viewmodel.HomeViewModel
import net.zackzhang.code.haze.weather.view.card.*
import net.zackzhang.code.haze.weather.view.ui.ColoredPaddingItemDecoration
import net.zackzhang.code.haze.weather.viewmodel.WeatherViewModel

class WeatherFragment : Fragment() {

    private val viewModel by viewModels<WeatherViewModel>()

    private val activityViewModel by activityViewModels<HomeViewModel>()

    private val cardAdapter = CardAdapter { type, parent ->
        when (type) {
            CARD_TYPE_WEATHER_HEAD -> WeatherHeadCard(parent)
            CARD_TYPE_WEATHER_HOURLY_ROW -> WeatherHourlyRowCard(parent)
            CARD_TYPE_WEATHER_TITLE -> WeatherTitleCard(parent)
            CARD_TYPE_WEATHER_DAILY -> WeatherDailyCard(parent)
            CARD_TYPE_WEATHER_CURRENT -> WeatherCurrentCard(parent)
            // Other cards
            else -> null
        }
    }

    private var navigationInset = 0

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
            (layoutManager as? GridLayoutManager)?.run {
                cardAdapter.spanCount = spanCount
                spanSizeLookup { cardAdapter.getSpanSize(it) }
            }
            adapter = cardAdapter
            addItemDecoration(ColoredPaddingItemDecoration(context.getDimension(R.dimen.dp_10), {
                cardAdapter.isDecorationExists(it)
            }, context.getColor(R.color.white_opacity_50), {
                cardAdapter.needBackground(it)
            }))
        }

        viewModel.observeCard(viewLifecycleOwner) {
            binding.root.isRefreshing = false
            cardAdapter.setCardData(it)
            leaveSpaceForNavigationInset()
        }
        viewModel.observeEvent(viewLifecycleOwner) {
            when (it.name) {
                EVENT_CITY_LOADED -> {
                    // 通知 HomeActivity 刷新城市
                    activityViewModel.notifyCityLoaded(it.data as? CityWeatherEntity)
                }
                EVENT_THEME_CHANGED -> (it.data as ThemeEntity).run {
                    binding.root.setColorSchemeColors(backgroundColor)
                    activityViewModel.notifyEvent(EVENT_THEME_CHANGED, this)
                }
            }
        }
        activityViewModel.observeEvent(viewLifecycleOwner) {
            when (it.name) {
                EVENT_CITY_CHANGED -> {
                    cardAdapter.setCardData(emptyList())
                    viewModel.notifyRefreshing((it.data as CityWeatherEntity).id)
                }
                EVENT_WINDOW_INSETS_APPLIED -> {
                    navigationInset = (it.data as SystemBarInsets).navigation
                    leaveSpaceForNavigationInset()
                }
            }
        }
        viewModel.notifyLoadingData()

        return binding.root
    }

    private fun leaveSpaceForNavigationInset() {
        if (navigationInset <= 0) return
        val cd = SpaceCardData(vertical = navigationInset)
        val pos = cardAdapter.lastCardPosition
        if (pos < 0 || !cardAdapter.isCardTypeMatch(pos, CARD_TYPE_SPACE)) {
            cardAdapter.addCard(cd)
        } else {
            cardAdapter.replaceCard(cd, pos)
        }
    }

    private inline fun GridLayoutManager.spanSizeLookup(crossinline lookup: (Int) -> Int) {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = lookup(position)
        }
    }
}