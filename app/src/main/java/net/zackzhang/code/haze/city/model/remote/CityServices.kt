package net.zackzhang.code.haze.city.model.remote

import net.zackzhang.code.haze.city.model.entity.CityEntity
import net.zackzhang.code.haze.common.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface CityServices {

    @GET("lookup")
    suspend fun getSearchAssociation(
        @Query(Constants.QWEATHER_PARAM_LOCATION) location: String,
        @Query(Constants.QWEATHER_PARAM_PUBLIC_ID) publicId: String,
        @Query(Constants.QWEATHER_PARAM_TIME) time: String,
        @Query(Constants.QWEATHER_PARAM_SIGNATURE) signature: String
    ): List<CityEntity>
}