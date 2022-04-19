package net.zackzhang.code.haze.base.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    protected val eventLiveData by lazy {
        EventLiveData()
    }

    private val savedEventMap = hashMapOf<String, Any>()

    fun observeEvent(owner: LifecycleOwner, observer: (Event) -> Unit) {
        eventLiveData.observe(owner, observer)
    }

    fun notifyEvent(name: String, data: Any, save: Boolean = true) {
        if (save) {
            savedEventMap[name] = data
        }
        eventLiveData.value = Event(name, data)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getSavedEvent(name: String) = savedEventMap[name] as? T
}