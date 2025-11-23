package net.zackzhang.code.haze.models

import android.os.Parcelable
import androidx.activity.result.ActivityResult
import androidx.annotation.ColorRes
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.parcelize.Parcelize

abstract class ModelBase : ViewModel() {

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

    fun notifyEvent(event: Event) {
        eventLiveData.value = event
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

class EventLiveData : MutableLiveData<Event?>() {

    internal fun observe(owner: LifecycleOwner, observer: (Event) -> Unit) {
        // 在每次订阅时都将 value 置为 null
        // 下面拦截了 null，因此不会通知到观察者
        // Event 数据应是一次性的，通知到所有观察者后就无用了
        value = null
        super.observe(owner) {
            if (it != null) {
                observer(it)
            }
        }
    }
}

sealed interface Event {

    data object NetworkFailed : Event

    data class WindowInsetsApplied(val insets: SystemBarInsets) : Event

    data class ActivityFinish(val result: ActivityResult) : Event
}

@Parcelize
data class ThemeEntity(
    // ThemeEntity 会保存在 ViewModel 中，所以这里需要是与深浅色模式无关的资源 ID
    // 当深浅色模式切换后，重建时再去取对应的颜色
    @param:ColorRes val accentColor: Int,
): Parcelable

data class SystemBarInsets(
    val status: Int,
    val navigation: Int,
) {

    companion object {

        fun fromWindowInsets(insets: WindowInsetsCompat) =
            // 不要用 WindowInsetsCompat.Type.statusBars()
            // Freeform Window 模式的 top 是 0
            insets.getInsets(WindowInsetsCompat.Type.systemBars()).run {
                SystemBarInsets(top, bottom)
            }
    }
}