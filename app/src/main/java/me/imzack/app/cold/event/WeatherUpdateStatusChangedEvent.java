package me.imzack.app.cold.event;

/**
 * 当更新状态改变时应该发送此事件<br>
 * 1. 未更新->正在更新<br>
 * 2. 正在更新->更新结束（成功/失败）
 */
public class WeatherUpdateStatusChangedEvent {

    /** 状态：正在更新 */
    public static final int STATUS_ON_UPDATING = 0;
    /** 状态：更新结束（成功） */
    public static final int STATUS_UPDATED_SUCCESSFUL = 1;
    /** 状态：更新结束（失败） */
    public static final int STATUS_UPDATED_FAILED = -1;

    public int position;
    public String cityId;
    public int status;

    public WeatherUpdateStatusChangedEvent(int position, String cityId, int status) {
        this.position = position;
        this.cityId = cityId;
        this.status = status;
    }
}
