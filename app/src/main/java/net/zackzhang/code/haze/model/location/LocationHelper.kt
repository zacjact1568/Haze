package net.zackzhang.code.haze.model.location

import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import io.reactivex.Single
import net.zackzhang.code.haze.App
import net.zackzhang.code.haze.common.Constant
import net.zackzhang.code.haze.exception.AMapLocationServiceException
import net.zackzhang.code.haze.exception.NoEnoughPermissionsGrantedException
import net.zackzhang.code.haze.exception.SystemLocationServiceDisabledException
import net.zackzhang.code.haze.util.SystemUtil
import net.zackzhang.code.haze.view.fragment.LocationServicePermissionsFragment

class LocationHelper {

    /** 获取高德定位数据 */
    fun getAMapLocationData() = Single.create<Pair<Double, Double>> { emitter ->
        val locationMode = SystemUtil.locationMode
        if (locationMode == Constant.LOCATION_MODE_NONE) {
            // 系统位置服务未开启
            emitter.onError(SystemLocationServiceDisabledException())
        } else if (!SystemUtil.checkPermissions(LocationServicePermissionsFragment.PERMISSIONS)) {
            // 未授予全部必要权限
            emitter.onError(NoEnoughPermissionsGrantedException())
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
