package net.zackzhang.code.haze.city.view

import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePaddingRelative
import androidx.core.widget.addTextChangedListener
import net.zackzhang.code.haze.city.viewmodel.CityViewModel
import net.zackzhang.code.haze.common.constant.EVENT_ACTIVITY_FINISH
import net.zackzhang.code.haze.databinding.ActivityCityBinding

class CityActivity : AppCompatActivity() {

    private val viewModel by viewModels<CityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val binding = ActivityCityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.vToolbar) { v, insets ->
            v.updatePaddingRelative(top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top)
            WindowInsetsCompat.CONSUMED
        }
        binding.vBack.setOnClickListener { finish() }
        binding.vSearchBar.addTextChangedListener {
            viewModel.notifySearchInputChanged(it?.toString().orEmpty())
        }
        viewModel.observeEvent(this) {
            when (it.name) {
                EVENT_ACTIVITY_FINISH -> {
                    val ar = it.data as ActivityResult
                    setResult(ar.resultCode, ar.data)
                    finish()
                }
            }
        }
    }
}