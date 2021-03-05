package net.zackzhang.code.haze.air.model.remote

import net.zackzhang.code.haze.air.model.entity.AirEntity
import net.zackzhang.code.haze.common.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface AirServices {

    @GET("now")
    suspend fun getNow(
        @Query(Constants.QWEATHER_PARAM_LOCATION) location: String,
        @Query(Constants.QWEATHER_PARAM_PUBLIC_ID) publicId: String,
        @Query(Constants.QWEATHER_PARAM_TIME) time: String,
        @Query(Constants.QWEATHER_PARAM_SIGNATURE) signature: String
    ): AirEntity
}