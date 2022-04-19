package net.zackzhang.code.haze.city.model.remote

import net.zackzhang.code.haze.city.model.entity.CityEntity
import net.zackzhang.code.haze.base.constant.QWEATHER_PARAM_LOCATION
import net.zackzhang.code.haze.base.constant.QWEATHER_PARAM_PUBLIC_ID
import net.zackzhang.code.haze.base.constant.QWEATHER_PARAM_SIGNATURE
import net.zackzhang.code.haze.base.constant.QWEATHER_PARAM_TIME
import retrofit2.http.GET
import retrofit2.http.Query

interface CityServices {

    @GET("lookup")
    suspend fun getSearchAssociation(
        @Query(QWEATHER_PARAM_LOCATION) location: String,
        @Query(QWEATHER_PARAM_PUBLIC_ID) publicId: String,
        @Query(QWEATHER_PARAM_TIME) time: String,
        @Query(QWEATHER_PARAM_SIGNATURE) signature: String
    ): List<CityEntity>
}