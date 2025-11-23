package net.zackzhang.code.haze.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.utils.getDimension
import net.zackzhang.code.haze.utils.showToast
import net.zackzhang.code.haze.components.recycler.CardAdapter
import net.zackzhang.code.haze.databinding.FragmentWeatherBinding
import net.zackzhang.code.haze.models.HomeViewModel
import net.zackzhang.code.haze.components.recycler.cards.WeatherCurrentCard
import net.zackzhang.code.haze.components.recycler.cards.WeatherDailyCard
import net.zackzhang.code.haze.components.recycler.cards.WeatherHeadCard
import net.zackzhang.code.haze.components.recycler.cards.WeatherHourlyRowCard
import net.zackzhang.code.haze.components.recycler.cards.WeatherTitleCard
import net.zackzhang.code.haze.components.recycler.ColoredPaddingItemDecoration
import net.zackzhang.code.haze.components.recycler.cards.CardType
import net.zackzhang.code.haze.components.recycler.cards.SpaceCardConfig
import net.zackzhang.code.haze.models.CityChangedEvent
import net.zackzhang.code.haze.models.CityLoadedEvent
import net.zackzhang.code.haze.models.Event
import net.zackzhang.code.haze.models.WeatherViewModel

class WeatherFragment : Fragment() {

    private val viewModel by viewModels<WeatherViewModel>()

    private val activityViewModel by activityViewModels<HomeViewModel>()

    private val cardAdapter = CardAdapter { type, parent ->
        when (type) {
            CardType.WEATHER_HEAD.ordinal -> WeatherHeadCard(parent)
            CardType.WEATHER_HOURLY_ROW.ordinal -> WeatherHourlyRowCard(parent)
            CardType.WEATHER_TITLE.ordinal -> WeatherTitleCard(parent)
            CardType.WEATHER_DAILY.ordinal -> WeatherDailyCard(parent)
            CardType.WEATHER_CURRENT.ordinal -> WeatherCurrentCard(parent)
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
            }, context.getColor(R.color.weather_body_background), {
                cardAdapter.needBackground(it)
            }))
        }

        viewModel.observeCard(viewLifecycleOwner) {
            binding.root.isRefreshing = false
            cardAdapter.setCardConfigs(it)
            leaveSpaceForNavigationInset()
        }
        viewModel.observeEvent(viewLifecycleOwner) {
            when (it) {
                is CityLoadedEvent -> {
                    // 通知 HomeActivity 刷新城市
                    activityViewModel.notifyCityLoaded(it.entity)
                }
                Event.NetworkFailed -> {
                    binding.root.isRefreshing = false
                    showToast(R.string.toast_network_failed)
                }
                else -> {}
            }
        }
        viewModel.observeTheme(viewLifecycleOwner) {
            binding.root.run {
                setColorSchemeColors(context.getColor(it.accentColor))
            }
            activityViewModel.notifyThemeChanged(it)
        }
        activityViewModel.observeEvent(viewLifecycleOwner) {
            when (it) {
                is CityChangedEvent -> {
                    cardAdapter.setCardConfigs(emptyList())
                    viewModel.notifyRefreshing(it.city.id)
                }
                is Event.WindowInsetsApplied -> {
                    navigationInset = it.insets.navigation
                    leaveSpaceForNavigationInset()
                }
                else -> {}
            }
        }
        viewModel.notifyLoadingData()

        return binding.root
    }

    private fun leaveSpaceForNavigationInset() {
        if (navigationInset <= 0) return
        val cd = SpaceCardConfig(vertical = navigationInset)
        val pos = cardAdapter.lastCardPosition
        if (pos < 0) return
        if (!cardAdapter.isCardTypeMatch(pos, CardType.SPACE)) {
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