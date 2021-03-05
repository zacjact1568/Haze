package net.zackzhang.code.haze.home.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import net.zackzhang.code.haze.city.model.entity.CityWeatherEntity
import net.zackzhang.code.haze.city.view.CityActivity
import net.zackzhang.code.haze.common.Constants
import net.zackzhang.code.haze.databinding.ActivityHomeBinding
import net.zackzhang.code.haze.home.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {

    private val viewModel by viewModels<HomeViewModel>()

    private val cityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        when (it.resultCode) {
            Constants.RESULT_CODE_CITY_NEW -> {
                val city = it.data?.getParcelableExtra<CityWeatherEntity>(Constants.CITY)
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
        ViewCompat.setOnApplyWindowInsetsListener(binding.vToolbar) { v, insets ->
            v.updatePaddingRelative(top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top)
            WindowInsetsCompat.CONSUMED
        }
        binding.vCities.setOnClickListener {
            cityLauncher.launch(Intent(this, CityActivity::class.java))
        }
        binding.vCityName.text = viewModel.cityName ?: Constants.PLACEHOLDER
        viewModel.observeEvent(this) {
            when (it.name) {
                Constants.EVENT_DATA_LOADED, Constants.EVENT_CITY_CHANGED -> {
                    binding.vCityName.text = (it.data as? CityWeatherEntity)?.name ?: Constants.PLACEHOLDER
                }
            }
        }
    }
}