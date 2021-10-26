package net.zackzhang.code.haze.city.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import net.zackzhang.code.haze.city.model.entity.CityWeatherEntity
import net.zackzhang.code.haze.city.view.card.CitySearchAssociationCard
import net.zackzhang.code.haze.city.viewmodel.CitySearchAssociationViewModel
import net.zackzhang.code.haze.city.viewmodel.CityViewModel
import net.zackzhang.code.haze.common.constant.CARD_TYPE_CITY_SEARCH_ASSOCIATION
import net.zackzhang.code.haze.common.constant.EVENT_CITY_SELECTED
import net.zackzhang.code.haze.common.view.CardAdapter
import net.zackzhang.code.haze.databinding.FragmentCitySearchAssociationBinding

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCitySearchAssociationBinding.inflate(inflater, container, false)
        binding.vAssociationList.adapter = adapter
        viewModel.observeCard(viewLifecycleOwner) {
            adapter.setCardData(it)
        }
        viewModel.observeEvent(viewLifecycleOwner) {
            when (it.name) {
                EVENT_CITY_SELECTED -> activityViewModel.notifyFinish(it.data as CityWeatherEntity?)
            }
        }
        activityViewModel.observeSearchInput(viewLifecycleOwner) {
            viewModel.notifySearching(it)
        }
        return binding.root
    }
}