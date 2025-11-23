package net.zackzhang.code.haze.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.updatePaddingRelative
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import net.zackzhang.code.haze.components.recycler.cards.CitySearchAssociationCard
import net.zackzhang.code.haze.models.CitySearchAssociationViewModel
import net.zackzhang.code.haze.models.CityViewModel
import net.zackzhang.code.haze.utils.hideSoftInput
import net.zackzhang.code.haze.components.recycler.CardAdapter
import net.zackzhang.code.haze.components.recycler.cards.CardType
import net.zackzhang.code.haze.databinding.FragmentCitySearchAssociationBinding
import net.zackzhang.code.haze.models.CitySelectedEvent
import net.zackzhang.code.haze.models.Event
import kotlin.math.abs

class CitySearchAssociationFragment : Fragment() {

    private lateinit var viewBinding: FragmentCitySearchAssociationBinding

    private val viewModel by viewModels<CitySearchAssociationViewModel>()

    private val activityViewModel by activityViewModels<CityViewModel>()

    private val adapter = CardAdapter { type, parent ->
        when (type) {
            CardType.CITY_SEARCH_ASSOCIATION.ordinal -> CitySearchAssociationCard(parent) {
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
        viewBinding = binding
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
                v.hideSoftInput()
            }
            false
        }
        viewModel.observeCard(viewLifecycleOwner) {
            // 取消加载动画
            binding.vSearchStatus.run {
                // 注意不要在这里判断 isAnimating，它（可能）是真实反应动画是否在播放，而不是是否已调用 playAnimation
                // 例如在 recreate 后的 start 阶段，LiveData 会用恢复的数据回调该 observer
                // 由于此时还没有 resume，vSearchStatus 还未绘制，动画肯定不会播放
                // 也就是说即使调用过 playAnimation，此时 isAnimating 也是 false
                // 这样会造成 isInvisible = true 未调用，vSearchStatus 不能正确隐藏
                cancelAnimationAndHide()
            }
            adapter.setCardConfigs(it)
            // 搜索结果列表不为空，或搜索词为空，才隐藏空文案
            binding.vNotFound.isInvisible = it.isNotEmpty() || viewModel.emptyInput
        }
        viewModel.observeEvent(viewLifecycleOwner) {
            when (it) {
                is CitySelectedEvent -> activityViewModel.notifyFinish(it.entity)
                else -> {}
            }
        }
        activityViewModel.observeSearchInput(viewLifecycleOwner) {
            // 只要输入变化就隐藏空文案
            binding.vNotFound.isInvisible = true
            // 现有搜索结果列表为空，且输入文本不为空，播放加载动画
            if (viewModel.emptyList && it.isNotEmpty()) {
                binding.vSearchStatus.playAnimationAndShow()
            }
            viewModel.notifySearching(it)
        }
        activityViewModel.observeEvent(viewLifecycleOwner) {
            when (it) {
                is Event.WindowInsetsApplied ->
                    binding.vAssociationList.updatePaddingRelative(bottom = it.insets.navigation)
                else -> {}
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewBinding.vSearchStatus.resumeAnimationIfVisible()
    }

    override fun onStop() {
        super.onStop()
        viewBinding.vSearchStatus.pauseAnimationIfVisible()
    }
}