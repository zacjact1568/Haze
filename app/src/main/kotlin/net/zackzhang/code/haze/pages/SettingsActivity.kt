package net.zackzhang.code.haze.pages

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePaddingRelative
import net.zackzhang.code.haze.models.SystemBarInsets
import net.zackzhang.code.haze.models.ThemeEntity
import net.zackzhang.code.haze.databinding.ActivitySettingsBinding
import net.zackzhang.code.haze.models.Event
import net.zackzhang.code.haze.definitions.Extra
import net.zackzhang.code.haze.models.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        IntentCompat.getParcelableExtra(
            intent,
            Extra.THEME.name,
            ThemeEntity::class.java,
        )?.let {
            viewModel.notifyThemeChanged(it)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            viewModel.notifyEvent(Event.WindowInsetsApplied(SystemBarInsets.fromWindowInsets(insets)))
            WindowInsetsCompat.CONSUMED
        }
        binding.vBack.setOnClickListener { finish() }

        viewModel.observeEvent(this) {
            when (it) {
                is Event.WindowInsetsApplied ->
                    binding.vToolbar.updatePaddingRelative(top = it.insets.status)
                else -> {}
            }
        }
        viewModel.observeTheme(this) {
            val accentColor = getColor(it.accentColor)
            binding.vToolbar.setBackgroundColor(accentColor)
        }
    }
}