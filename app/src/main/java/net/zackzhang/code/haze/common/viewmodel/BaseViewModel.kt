package net.zackzhang.code.haze.common.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import net.zackzhang.code.haze.common.constant.EVENT_WINDOW_INSETS_APPLIED
import net.zackzhang.code.haze.common.view.SystemBarInsets

open class BaseViewModel : ViewModel() {

    protected val eventLiveData by lazy {
        EventLiveData()
    }

    fun observeEvent(owner: LifecycleOwner, observer: (Event) -> Unit) {
        eventLiveData.observe(owner, observer)
    }

    fun notifyWindowInsetsApplied(insets: SystemBarInsets) {
        eventLiveData.value = Event(EVENT_WINDOW_INSETS_APPLIED, insets)
    }
}