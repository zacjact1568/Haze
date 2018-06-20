package me.imzack.app.cold.model.location

import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import io.reactivex.Single
import me.imzack.app.cold.App
import me.imzack.app.cold.common.Constant
import me.imzack.app.cold.exception.AMapLocationServiceException
import me.imzack.app.cold.exception.SystemLocationServiceDisabledException
import me.imzack.app.cold.util.SystemUtil

class LocationHelper {

    /** 获取高德定位数据 */
    fun getAMapLocationData() = Single.create<Pair<Double, Double>> { emitter ->
        val locationMode = SystemUtil.locationMode
        if (locationMode == Constant.LOCATION_MODE_NONE) {
            // 系统位置服务未开启
            emitter.onError(SystemLocationServiceDisabledException())
        } else {
            val client = AMapLocationClient(App.context)
            // 当外部调用 dispose 时，停止定位服务
            emitter.setCancellable { client.onDestroy() }
            client.setLocationOption(
                    AMapLocationClientOption()
                            .setLocationMode(when (locationMode) {
                                Constant.LOCATION_MODE_HIGH_ACCURACY -> AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
                                Constant.LOCATION_MODE_BATTERY_SAVING -> AMapLocationClientOption.AMapLocationMode.Battery_Saving
                                Constant.LOCATION_MODE_DEVICE_SENSORS -> AMapLocationClientOption.AMapLocationMode.Device_Sensors
                                else -> throw IllegalArgumentException("Wrong location mode")
                            })
                            .setOnceLocationLatest(true)
            )
            client.setLocationListener {
                when {
                    it == null -> emitter.onError(AMapLocationServiceException(-1, "Returned a null object"))
                    it.errorCode == 0 -> emitter.onSuccess(Pair(it.longitude, it.latitude))
                    else -> emitter.onError(AMapLocationServiceException(it.errorCode, it.errorInfo))
                }
                // 销毁定位客户端
                client.onDestroy()
            }
            client.startLocation()
        }
    }!!
}
