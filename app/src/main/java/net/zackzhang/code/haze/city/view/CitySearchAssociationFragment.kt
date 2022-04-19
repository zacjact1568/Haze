package net.zackzhang.code.haze.city.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.updatePaddingRelative
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import net.zackzhang.code.haze.city.model.entity.CityWeatherEntity
import net.zackzhang.code.haze.city.view.card.CitySearchAssociationCard
import net.zackzhang.code.haze.city.viewmodel.CitySearchAssociationViewModel
import net.zackzhang.code.haze.city.viewmodel.CityViewModel
import net.zackzhang.code.haze.common.view.CardAdapter
import net.zackzhang.code.haze.base.view.SystemBarInsets
import net.zackzhang.code.haze.common.constant.CARD_TYPE_CITY_SEARCH_ASSOCIATION
import net.zackzhang.code.haze.common.constant.EVENT_CITY_SELECTED
import net.zackzhang.code.haze.common.constant.EVENT_WINDOW_INSETS_APPLIED
import net.zackzhang.code.haze.databinding.FragmentCitySearchAssociationBinding
import kotlin.math.abs

class CitySearchAssociationFragment : Fragment() {

    private val viewModel by viewModels<CitySearchAssociationViewModel>()

    private val activityViewModel by activityViewModels<CityViewModel>()

    private val adapter = CardAdapter { type, parent ->
        when (type) {
            CARD_TYPE_CITY_SEARCH_ASSOCIATION -> CitySearchAssociationCard(parent) {
                viewModel.notifySelected(it)
            }
            else -> null
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCitySearchAssociationBinding.inflate(inflater, container, false)
        binding.vAssociationList.adapter = adapter
        binding.vAssociationList.setOnTouchListener { v, event ->
            // 在列表上向上滑动时，收起软键盘
            if (!viewModel.emptyList
                && event.action == MotionEvent.ACTION_MOVE
                && event.historySize > 0
                // y 轴偏移大于 10px
                && event.getHistoricalY(0) - event.y > 10
                // x 轴偏移小于 10px
                && abs(event.getHistoricalX(0) - event.x) < 10) {
                context?.getSystemService(InputMethodManager::class.java)
                    ?.hideSoftInputFromWindow(v.windowToken, 0)
            }
            false
        }
        viewModel.observeCard(viewLifecycleOwner) {
            binding.vSearchStatus.run {
                // 如果动画正在播放，取消它
                if (isAnimating) {
                    cancelAnimation()
                    visibility = View.INVISIBLE
                }
            }
            adapter.setCardData(it)
            // 搜索结果列表为空，且搜索词不为空，才显示空文案
            binding.vNotFound.visibility =
                if (it.isEmpty() && !viewModel.emptyInput) View.VISIBLE else View.INVISIBLE
        }
        viewModel.observeEvent(viewLifecycleOwner) {
            when (it.name) {
                EVENT_CITY_SELECTED -> activityViewModel.notifyFinish(it.data as CityWeatherEntity?)
            }
        }
        activityViewModel.observeSearchInput(viewLifecycleOwner) {
            // 只要输入变化就隐藏空文案
            binding.vNotFound.visibility = View.INVISIBLE
            // 现有搜索结果列表为空，且输入文本不为空，播放加载动画
            if (viewModel.emptyList && it.isNotEmpty()) {
                binding.vSearchStatus.run {
                    visibility = View.VISIBLE
                    playAnimation()
                }
            }
            viewModel.notifySearching(it)
        }
        activityViewModel.observeEvent(viewLifecycleOwner) {
            when (it.name) {
                EVENT_WINDOW_INSETS_APPLIED ->
                    binding.vAssociationList.updatePaddingRelative(bottom = (it.data as SystemBarInsets).navigation)
            }
        }
        return binding.root
    }
}