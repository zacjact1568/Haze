package net.zackzhang.code.haze.pages

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePaddingRelative
import net.zackzhang.code.haze.models.sources.CityWeatherEntity
import net.zackzhang.code.haze.definitions.RESULT_CODE_CITY_NEW
import net.zackzhang.code.haze.models.SystemBarInsets
import net.zackzhang.code.haze.databinding.ActivityHomeBinding
import net.zackzhang.code.haze.models.Event
import net.zackzhang.code.haze.models.HomeViewModel
import net.zackzhang.code.haze.definitions.Extra
import net.zackzhang.code.util.requireNotNull

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
                val intent = requireNotNull(
                    it.data,
                    "`RESULT_CODE_CITY_NEW` result must have an intent",
                )
                val city = requireNotNull(
                    IntentCompat.getParcelableExtra(
                        intent,
                        Extra.CITY.name,
                        CityWeatherEntity::class.java,
                    ),
                    "`RESULT_CODE_CITY_NEW` result must have a `CityWeatherEntity` extra",
                )
                viewModel.notifyCityChanged(city)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(viewBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.root) { _, insets ->
            viewModel.notifyEvent(Event.WindowInsetsApplied(SystemBarInsets.fromWindowInsets(insets)))
            WindowInsetsCompat.CONSUMED
        }
        viewBinding.vLoading.playAnimation()
        viewBinding.vCities.setOnClickListener {
            cityLauncher.launch(
                Intent(this, CityActivity::class.java)
                .putExtra(Extra.THEME.name, viewModel.theme))
        }
        viewBinding.vSettings.setOnClickListener {
            startActivity(
                Intent(this, SettingsActivity::class.java)
                .putExtra(Extra.THEME.name, viewModel.theme))
        }

        viewModel.observeCity(this) {
            if (it != null) {
                viewBinding.vCityName.text = it.name ?: "--"
                viewBinding.vLoading.cancelAnimationAndGone()
                viewBinding.vWeatherGroup.isVisible = true
            } else {
                viewBinding.vCities.performClick()
            }
        }
        viewModel.observeEvent(this) {
            when (it) {
                is Event.WindowInsetsApplied ->
                    viewBinding.vToolbar.updatePaddingRelative(top = it.insets.status)
                else -> {}
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