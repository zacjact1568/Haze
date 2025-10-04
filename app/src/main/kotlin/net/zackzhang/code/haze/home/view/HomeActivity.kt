package net.zackzhang.code.haze.home.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.*
import net.zackzhang.code.haze.city.model.entity.CityWeatherEntity
import net.zackzhang.code.haze.city.view.CityActivity
import net.zackzhang.code.haze.common.view.SystemBarInsets
import net.zackzhang.code.haze.common.constant.*
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
        viewBinding.vLoading.playAnimation()
        viewBinding.vCities.setOnClickListener {
            cityLauncher.launch(Intent(this, CityActivity::class.java)
                .putExtra(EXTRA_THEME, viewModel.theme))
        }
        viewBinding.vSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java)
                .putExtra(EXTRA_THEME, viewModel.theme))
        }

        viewModel.observeCity(this) {
            if (it != null) {
                viewBinding.vCityName.text = it.name ?: PLACEHOLDER
                viewBinding.vLoading.cancelAnimationAndGone()
                viewBinding.vWeatherGroup.isVisible = true
            } else {
                viewBinding.vCities.performClick()
            }
        }
        viewModel.observeEvent(this) {
            when (it.name) {
                EVENT_WINDOW_INSETS_APPLIED ->
                    viewBinding.vToolbar.updatePaddingRelative(top = (it.data as SystemBarInsets).status)
            }
        }
        viewModel.observeTheme(this) {
            val accentColor = getColor(it.accentColor)
            viewBinding.root.setBackgroundColor(accentColor)
        }
    }

    override fun onStart() {
        super.onStart()
        viewBinding.vLoading.resumeAnimationIfVisible()
    }

    override fun onStop() {
        super.onStop()
        viewBinding.vLoading.pauseAnimationIfVisible()
    }
}