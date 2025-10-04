package net.zackzhang.code.haze.common.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.zackzhang.code.haze.common.view.ThemeEntity

abstract class BaseViewModel : ViewModel() {

    val theme get() = themeLiveData.value

    private val eventLiveData by lazy {
        EventLiveData()
    }

    private val themeLiveData by lazy {
        MutableLiveData<ThemeEntity>()
    }

    fun observeEvent(owner: LifecycleOwner, observer: (Event) -> Unit) {
        eventLiveData.observe(owner, observer)
    }

    fun observeTheme(owner: LifecycleOwner, observer: (ThemeEntity) -> Unit) {
        themeLiveData.observe(owner, observer)
    }

    fun notifyEvent(name: String, data: Any?) {
        eventLiveData.value = Event(name, data)
    }

    /**
     * - 在主题改变时调用
     * - 在新的 Activity 启动时向它的 ViewModel 设置主题
     */
    fun notifyThemeChanged(theme: ThemeEntity) {
        themeLiveData.value = theme
        onThemeChanged()
    }

    protected open fun onThemeChanged() {

    }
}