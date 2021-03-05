package net.zackzhang.code.haze.model.database.dao

import androidx.room.*
import io.reactivex.Single
import net.zackzhang.code.haze.common.Constant
import net.zackzhang.code.haze.model.database.entity.BasicEntity

@Dao
interface BasicDao {

    @Insert
    fun insert(basicEntity: BasicEntity)

    @Update
    fun update(basicEntity: BasicEntity)

    @Query("DELETE FROM ${Constant.BASIC} WHERE ${Constant.CITY_ID} = :cityId")
    fun delete(cityId: String)

    @Query("SELECT * FROM ${Constant.BASIC} ORDER BY ${Constant.ADD_TIME}")
    // 返回 Single，只发射一次事件
    // 如果使用 Flowable，后续数据库中与此查询相关的数据有变化，都会再次发射事件
    // 如果使用 Maybe，其较 Single 多了 onComplete，有点多余
    fun loadAll(): Single<Array<BasicEntity>>
}