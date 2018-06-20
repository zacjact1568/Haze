package me.imzack.app.cold.event

/** 当天气更新状态改变时应该发送此事件 */
class WeatherUpdateStatusChangedEvent(
        eventSource: String,
        cityId: String,
        position: Int,
        val status: Int,
        // 错误信息，当 status 为 STATUS_FAILED 时不为空
        val error: Throwable? = null
) : BaseEvent(eventSource, cityId, position) {

    companion object {
        /** 状态：正在更新 */
        const val STATUS_UPDATING = 0
        /**
         * 状态：定位完成
         *
         * 注：只有完成以下 3 个步骤才算定位完成：
         * 1. 通过高德定位获取到经纬度
         * 2. 通过和风天气城市查询获取到城市 id
         * 3. 将当前位置的城市信息存入数据库
         */
        const val STATUS_LOCATED = 1
        /** 状态：更新完成 */
        const val STATUS_UPDATED = 2
        /** 状态：更新失败 */
        const val STATUS_FAILED = -1
        /** 状态：更新取消 */
        const val STATUS_CANCELED = -2
    }
}
