package net.zackzhang.code.haze.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import net.zackzhang.code.haze.components.recycler.CardAdapter
import net.zackzhang.code.haze.components.recycler.cards.CardType
import net.zackzhang.code.haze.databinding.FragmentSettingsContentBinding
import net.zackzhang.code.haze.components.recycler.cards.SettingsPreferenceBasicCard
import net.zackzhang.code.haze.components.recycler.cards.SettingsSwitchPreferenceCard
import net.zackzhang.code.haze.models.SettingsContentViewModel
import net.zackzhang.code.haze.models.SettingsViewModel

class SettingsContentFragment : Fragment() {

    private val viewModel by viewModels<SettingsContentViewModel>()

    private val activityViewModel by activityViewModels<SettingsViewModel>()

    private val cardAdapter = CardAdapter({
        viewModel.notifyPreferenceClicked(it)
    }) { type, parent ->
        when (type) {
            CardType.SETTINGS_INFO_PREFERENCE.ordinal -> SettingsPreferenceBasicCard(parent)
            CardType.SETTINGS_SWITCH_PREFERENCE.ordinal -> SettingsSwitchPreferenceCard(parent) { position, checked ->
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
            cardAdapter.setCardConfigs(it)
        }
        activityViewModel.observeTheme(viewLifecycleOwner) {
            viewModel.notifyThemeChanged(it)
        }

        viewModel.notifyLoadingData()

        return binding.root
    }
}