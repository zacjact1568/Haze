package net.zackzhang.code.haze.city.view

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.EditText
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
import net.zackzhang.code.haze.common.constant.EVENT_WINDOW_INSETS_APPLIED
import net.zackzhang.code.haze.common.constant.EXTRA_THEME
import net.zackzhang.code.haze.common.model.entity.ThemeEntity
import net.zackzhang.code.haze.common.view.SystemBarInsets
import net.zackzhang.code.haze.databinding.ActivityCityBinding

class CityActivity : AppCompatActivity() {

    private val viewModel by viewModels<CityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val binding = ActivityCityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            viewModel.notifyWindowInsetsApplied(SystemBarInsets.fromWindowInsets(insets))
            WindowInsetsCompat.CONSUMED
        }
        binding.updateViewsTheme(intent.getParcelableExtra(EXTRA_THEME))
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
        // 请求焦点以弹出键盘
        binding.vSearchBar.requestFocus()
    }

    private fun ActivityCityBinding.updateViewsTheme(theme: ThemeEntity?) {
        if (theme == null) return
        vToolbar.setBackgroundColor(theme.backgroundColor)
        vBack.imageTintList = ColorStateList.valueOf(theme.foregroundColor)
        vTitle.setTextColor(theme.foregroundColor)
        vSearchBar.backgroundTintList = ColorStateList.valueOf(theme.foregroundColor)
        vSearchIcon.imageTintList = ColorStateList.valueOf(theme.backgroundColor)
    }

    private inline fun EditText.addAfterTextChangedListener(crossinline listener: (String) -> Unit) {
        addTextChangedListener { listener(it?.toString().orEmpty()) }
    }

    private fun EditText.clearText() {
        setText("")
    }
}