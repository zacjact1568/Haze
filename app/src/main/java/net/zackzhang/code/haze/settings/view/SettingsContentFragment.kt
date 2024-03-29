package net.zackzhang.code.haze.settings.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import net.zackzhang.code.haze.common.view.CardAdapter
import net.zackzhang.code.haze.common.constant.CARD_TYPE_SETTINGS_INFO_PREFERENCE
import net.zackzhang.code.haze.common.constant.CARD_TYPE_SETTINGS_SWITCH_PREFERENCE
import net.zackzhang.code.haze.databinding.FragmentSettingsContentBinding
import net.zackzhang.code.haze.settings.view.card.SettingsPreferenceBaseCard
import net.zackzhang.code.haze.settings.view.card.SettingsSwitchPreferenceCard
import net.zackzhang.code.haze.settings.viewmodel.SettingsContentViewModel
import net.zackzhang.code.haze.settings.viewmodel.SettingsViewModel

class SettingsContentFragment : Fragment() {

    private val viewModel by viewModels<SettingsContentViewModel>()

    private val activityViewModel by activityViewModels<SettingsViewModel>()

    private val cardAdapter = CardAdapter({
        viewModel.notifyPreferenceClicked(it)
    }) { type, parent ->
        when (type) {
            CARD_TYPE_SETTINGS_INFO_PREFERENCE -> SettingsPreferenceBaseCard(parent)
            CARD_TYPE_SETTINGS_SWITCH_PREFERENCE -> SettingsSwitchPreferenceCard(parent) { position, checked ->
                viewModel.notifyPreferenceUpdated(position, checked)
            }
            else -> null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSettingsContentBinding.inflate(inflater, container, false)
        binding.vPreferenceList.adapter = cardAdapter

        viewModel.observeCard(viewLifecycleOwner) {
            cardAdapter.setCardData(it)
        }
        activityViewModel.observeTheme(viewLifecycleOwner) {
            viewModel.notifyThemeChanged(it)
        }

        viewModel.notifyLoadingData()

        return binding.root
    }
}