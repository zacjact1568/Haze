package net.zackzhang.code.haze.home.view

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import net.zackzhang.code.haze.city.model.entity.CityWeatherEntity
import net.zackzhang.code.haze.city.view.CityActivity
import net.zackzhang.code.haze.common.constant.*
import net.zackzhang.code.haze.common.model.entity.ThemeEntity
import net.zackzhang.code.haze.common.view.SystemBarInsets
import net.zackzhang.code.haze.databinding.ActivityHomeBinding
import net.zackzhang.code.haze.home.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {

    private val viewModel by viewModels<HomeViewModel>()

    private val cityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        when (it.resultCode) {
            RESULT_CODE_CITY_NEW -> {
                val city = it.data?.getParcelableExtra<CityWeatherEntity>(CITY)
                if (city != null) {
                    viewModel.notifyCityChanged(city)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            viewModel.notifyWindowInsetsApplied(SystemBarInsets.fromWindowInsets(insets))
            WindowInsetsCompat.CONSUMED
        }
        binding.vCities.setOnClickListener {
            cityLauncher.launch(Intent(this, CityActivity::class.java)
                .putExtra(EXTRA_THEME, viewModel.theme))
        }
        binding.updateCityName(viewModel.cityName)
        binding.updateViewsTheme(viewModel.theme)
        viewModel.observeEvent(this) {
            when (it.name) {
                EVENT_DATA_LOADED, EVENT_CITY_CHANGED -> {
                    binding.updateCityName((it.data as? CityWeatherEntity)?.name)
                }
                EVENT_THEME_CHANGED -> {
                    binding.updateViewsTheme(it.data as? ThemeEntity)
                }
                EVENT_WINDOW_INSETS_APPLIED ->
                    binding.vToolbar.updatePaddingRelative(top = (it.data as SystemBarInsets).status)
            }
        }
    }

    private fun ActivityHomeBinding.updateCityName(cityName: String?) {
        vCityName.text = cityName ?: PLACEHOLDER
    }

    private fun ActivityHomeBinding.updateViewsTheme(theme: ThemeEntity?) {
        if (theme == null) return
        vToolbar.setBackgroundColor(theme.backgroundColor)
        vCities.imageTintList = ColorStateList.valueOf(theme.foregroundColor)
        vCityName.setTextColor(theme.foregroundColor)
        vSettings.imageTintList = ColorStateList.valueOf(theme.foregroundColor)
    }
}