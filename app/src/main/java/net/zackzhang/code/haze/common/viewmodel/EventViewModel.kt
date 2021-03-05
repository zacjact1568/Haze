package net.zackzhang.code.haze.common.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class EventViewModel : ViewModel() {

    protected val eventLiveData by lazy {
        EventLiveData()
    }

    fun observeEvent(owner: LifecycleOwner, observer: (Event) -> Unit) {
        eventLiveData.observe(owner, observer)
    }
}