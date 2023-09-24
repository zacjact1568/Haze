package net.zackzhang.code.haze.home.view

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.*
import net.zackzhang.code.haze.city.model.entity.CityWeatherEntity
import net.zackzhang.code.haze.city.view.CityActivity
import net.zackzhang.code.haze.common.view.ThemeEntity
import net.zackzhang.code.haze.common.view.SystemBarInsets
import net.zackzhang.code.haze.common.constant.*
import net.zackzhang.code.haze.common.util.pauseAnimationWhenPlaying
import net.zackzhang.code.haze.common.util.resumeAnimationWhenPause
import net.zackzhang.code.haze.databinding.ActivityHomeBinding
import net.zackzhang.code.haze.home.viewmodel.HomeViewModel
import net.zackzhang.code.haze.settings.view.SettingsActivity

class HomeActivity : AppCompatActivity() {

    private val viewBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<HomeViewModel>()

    private val cityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        when (it.resultCode) {
            RESULT_CANCELED -> {
                if (!viewModel.hasCity) {
                    finish()
                }
            }
            RESULT_CODE_CITY_NEW -> {
                val city = it.data?.let { intent ->
                    IntentCompat.getParcelableExtra(intent, CITY, CityWeatherEntity::class.java)
                }
                if (city != null) {
                    viewModel.notifyCityChanged(city)
                } else {
                    throw IllegalArgumentException("RESULT_CODE_CITY_NEW result should have a CityWeatherEntity extra")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(viewBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.root) { _, insets ->
            viewModel.notifyEvent(EVENT_WINDOW_INSETS_APPLIED, SystemBarInsets.fromWindowInsets(insets))
            WindowInsetsCompat.CONSUMED
        }
        viewBinding.updateViewsTheme(viewModel.getSavedEvent<ThemeEntity>(EVENT_THEME_CHANGED))
        viewBinding.vCities.setOnClickListener {
            cityLauncher.launch(Intent(this, CityActivity::class.java)
                .putExtra(EXTRA_THEME, viewModel.getSavedEvent<ThemeEntity>(EVENT_THEME_CHANGED)))
        }
        viewBinding.vSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java)
                .putExtra(EXTRA_THEME, viewModel.getSavedEvent<ThemeEntity>(EVENT_THEME_CHANGED)))
        }

        viewModel.observeCity(this) {
            if (it != null) {
                viewBinding.updateCityName(it.name)
                viewBinding.vLoading.apply {
                    cancelAnimation()
                    isVisible = false
                }
                viewBinding.vWeatherGroup.isVisible = true
            } else {
                viewBinding.vCities.performClick()
            }
        }
        viewModel.observeEvent(this) {
            when (it.name) {
                EVENT_THEME_CHANGED -> viewBinding.updateViewsTheme(it.data as ThemeEntity)
                EVENT_WINDOW_INSETS_APPLIED ->
                    viewBinding.vToolbar.updatePaddingRelative(top = (it.data as SystemBarInsets).status)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewBinding.vLoading.resumeAnimationWhenPause()
    }

    override fun onPause() {
        super.onPause()
        viewBinding.vLoading.pauseAnimationWhenPlaying()
    }

    private fun ActivityHomeBinding.updateCityName(cityName: String?) {
        vCityName.text = cityName ?: PLACEHOLDER
    }

    private fun ActivityHomeBinding.updateViewsTheme(theme: ThemeEntity?) {
        if (theme == null) return
        root.setBackgroundColor(theme.backgroundColor)
        vCities.imageTintList = ColorStateList.valueOf(theme.foregroundColor)
        vCityName.setTextColor(theme.foregroundColor)
        vSettings.imageTintList = ColorStateList.valueOf(theme.foregroundColor)
    }
}