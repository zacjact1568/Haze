package net.zackzhang.code.haze.settings.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePaddingRelative
import net.zackzhang.code.haze.common.view.ThemeEntity
import net.zackzhang.code.haze.common.view.SystemBarInsets
import net.zackzhang.code.haze.common.constant.EVENT_WINDOW_INSETS_APPLIED
import net.zackzhang.code.haze.common.constant.EXTRA_THEME
import net.zackzhang.code.haze.databinding.ActivitySettingsBinding
import net.zackzhang.code.haze.settings.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (IntentCompat.getParcelableExtra(intent, EXTRA_THEME, ThemeEntity::class.java))?.let {
            viewModel.notifyThemeChanged(it)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            viewModel.notifyEvent(EVENT_WINDOW_INSETS_APPLIED, SystemBarInsets.fromWindowInsets(insets))
            WindowInsetsCompat.CONSUMED
        }
        binding.vBack.setOnClickListener { finish() }

        viewModel.observeEvent(this) {
            when (it.name) {
                EVENT_WINDOW_INSETS_APPLIED ->
                    binding.vToolbar.updatePaddingRelative(top = (it.data as SystemBarInsets).status)
            }
        }
        viewModel.observeTheme(this) {
            val accentColor = getColor(it.accentColor)
            binding.vToolbar.setBackgroundColor(accentColor)
        }
    }
}