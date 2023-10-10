package net.zackzhang.code.haze.city.view

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePaddingRelative
import androidx.core.widget.addTextChangedListener
import net.zackzhang.code.haze.city.viewmodel.CityViewModel
import net.zackzhang.code.haze.common.view.ThemeEntity
import net.zackzhang.code.haze.common.view.SystemBarInsets
import net.zackzhang.code.haze.common.constant.EVENT_ACTIVITY_FINISH
import net.zackzhang.code.haze.common.constant.EVENT_WINDOW_INSETS_APPLIED
import net.zackzhang.code.haze.common.constant.EXTRA_THEME
import net.zackzhang.code.haze.common.util.showSoftInput
import net.zackzhang.code.haze.databinding.ActivityCityBinding

class CityActivity : AppCompatActivity() {

    private val viewModel by viewModels<CityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val binding = ActivityCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (IntentCompat.getParcelableExtra(intent, EXTRA_THEME, ThemeEntity::class.java))?.let {
            viewModel.notifyThemeChanged(it)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            viewModel.notifyEvent(EVENT_WINDOW_INSETS_APPLIED, SystemBarInsets.fromWindowInsets(insets))
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
            when (it.name) {
                EVENT_ACTIVITY_FINISH -> {
                    val ar = it.data as ActivityResult
                    setResult(ar.resultCode, ar.data)
                    finish()
                }
                EVENT_WINDOW_INSETS_APPLIED ->
                    binding.vToolbar.updatePaddingRelative(top = (it.data as SystemBarInsets).status)
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