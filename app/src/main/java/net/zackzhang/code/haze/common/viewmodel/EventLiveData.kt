package net.zackzhang.code.haze.common.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

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