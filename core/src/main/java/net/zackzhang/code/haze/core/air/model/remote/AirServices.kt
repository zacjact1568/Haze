package net.zackzhang.code.haze.core.air.model.remote

import net.zackzhang.code.haze.core.air.model.entity.AirEntity
import net.zackzhang.code.haze.base.constant.QWEATHER_PARAM_LOCATION
import net.zackzhang.code.haze.base.constant.QWEATHER_PARAM_PUBLIC_ID
import net.zackzhang.code.haze.base.constant.QWEATHER_PARAM_SIGNATURE
import net.zackzhang.code.haze.base.constant.QWEATHER_PARAM_TIME
import retrofit2.http.GET
import retrofit2.http.Query

interface AirServices {

    @GET("now")
    suspend fun getNow(
        @Query(QWEATHER_PARAM_LOCATION) location: String,
        @Query(QWEATHER_PARAM_PUBLIC_ID) publicId: String,
        @Query(QWEATHER_PARAM_TIME) time: String,
        @Query(QWEATHER_PARAM_SIGNATURE) signature: String
    ): AirEntity
}