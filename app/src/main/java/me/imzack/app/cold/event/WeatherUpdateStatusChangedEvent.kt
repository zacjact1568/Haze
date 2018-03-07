package me.imzack.app.cold.event

/**
 * 当更新状态改变时应该发送此事件
 *
 * 1. 未更新->正在更新
 *
 * 2. 正在更新->更新结束（成功/失败）
 */
class WeatherUpdateStatusChangedEvent(
        eventSource: String,
        cityId: String,
        position: Int,
        val status: Int
) : BaseEvent(eventSource, cityId, position) {

    companion object {
        /** 状态：正在更新  */
        const val STATUS_ON_UPDATING = 0
        /** 状态：更新结束（成功）  */
        const val STATUS_UPDATED_SUCCESSFUL = 1
        /** 状态：更新结束（失败）  */
        const val STATUS_UPDATED_FAILED = -1
    }
}
