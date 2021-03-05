package net.zackzhang.code.haze.model.network.service

import io.reactivex.Single
import net.zackzhang.code.haze.model.network.bean.HeWeatherSearch
import retrofit2.http.GET
import retrofit2.http.Query

/** 和风天气 Search API */
// 按 API 组织
interface HeWeatherSearchService {

    companion object {
        val TYPE_FIND = "find"
        //val TYPE_TOP = "top"
    }

    /** 城市信息 */
    @GET("find")
    fun getFindData(@Query("location") location: String, @Query("key") key: String): Single<HeWeatherSearch.Find>

    /** 热门城市 */
//    @GET("top")
//    fun getTopData(@Query("location") location: String, @Query("key") key: String): Single<HeWeatherSearch.Find>
}
