package net.zackzhang.code.haze.settings.view

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePaddingRelative
import net.zackzhang.code.haze.base.view.ThemeEntity
import net.zackzhang.code.haze.base.view.SystemBarInsets
import net.zackzhang.code.haze.common.constant.EVENT_THEME_CHANGED
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

        (intent.getParcelableExtra<ThemeEntity>(EXTRA_THEME))?.let {
            viewModel.notifyEvent(EVENT_THEME_CHANGED, it)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            viewModel.notifyEvent(EVENT_WINDOW_INSETS_APPLIED, SystemBarInsets.fromWindowInsets(insets))
            WindowInsetsCompat.CONSUMED
        }
        binding.updateViewsTheme(viewModel.getSavedEvent<ThemeEntity>(EVENT_THEME_CHANGED))
        binding.vBack.setOnClickListener { finish() }

        viewModel.observeEvent(this) {
            when (it.name) {
                EVENT_THEME_CHANGED -> binding.updateViewsTheme(it.data as ThemeEntity)
                EVENT_WINDOW_INSETS_APPLIED ->
                    binding.vToolbar.updatePaddingRelative(top = (it.data as SystemBarInsets).status)
            }
        }
    }

    private fun ActivitySettingsBinding.updateViewsTheme(theme: ThemeEntity?) {
        if (theme == null) return
        vToolbar.setBackgroundColor(theme.backgroundColor)
        vBack.imageTintList = ColorStateList.valueOf(theme.foregroundColor)
        vTitle.setTextColor(theme.foregroundColor)
    }
}