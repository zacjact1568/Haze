package net.zackzhang.code.haze.pages

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePaddingRelative
import androidx.core.widget.addTextChangedListener
import net.zackzhang.code.haze.models.CityViewModel
import net.zackzhang.code.haze.utils.showSoftInput
import net.zackzhang.code.haze.models.SystemBarInsets
import net.zackzhang.code.haze.models.ThemeEntity
import net.zackzhang.code.haze.databinding.ActivityCityBinding
import net.zackzhang.code.haze.models.Event
import net.zackzhang.code.haze.definitions.Extra

class CityActivity : AppCompatActivity() {

    private val viewModel by viewModels<CityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val binding = ActivityCityBinding.inflate(layoutInflater)
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
        binding.vSearchBar.addAfterTextChangedListener {
            binding.vSearchClear.visibility = if (it.isEmpty()) View.INVISIBLE else View.VISIBLE
            viewModel.notifySearchInputChanged(it)
        }
        binding.vSearchClear.setOnClickListener {
            binding.vSearchBar.clearText()
        }

        viewModel.observeEvent(this) {
            when (it) {
                is Event.ActivityFinish -> {
                    val ar = it.result
                    setResult(ar.resultCode, ar.data)
                    finish()
                }
                is Event.WindowInsetsApplied ->
                    binding.vToolbar.updatePaddingRelative(top = it.insets.status)
                else -> {}
            }
        }
        viewModel.observeTheme(this) {
            val accentColor = getColor(it.accentColor)
            binding.run {
                vToolbar.setBackgroundColor(accentColor)
                vSearchIcon.imageTintList = ColorStateList.valueOf(accentColor)
            }
        }

        // 请求焦点以弹出键盘
        binding.vSearchBar.requestFocus()
    }

    private inline fun EditText.addAfterTextChangedListener(crossinline listener: (String) -> Unit) {
        addTextChangedListener { listener(it?.toString().orEmpty()) }
    }

    private fun EditText.clearText() {
        setText("")
        showSoftInput()
    }
}