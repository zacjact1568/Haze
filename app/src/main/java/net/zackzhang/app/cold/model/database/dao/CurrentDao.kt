package net.zackzhang.app.cold.model.database.dao

import android.arch.persistence.room.*
import io.reactivex.Single
import net.zackzhang.app.cold.common.Constant
import net.zackzhang.app.cold.model.database.entity.CurrentEntity

@Dao
interface CurrentDao {

    @Insert
    fun insert(currentEntity: CurrentEntity)

    @Update
    fun update(currentEntity: CurrentEntity)

    @Query("DELETE FROM ${Constant.CURRENT} WHERE ${Constant.CITY_ID} = :cityId")
    fun delete(cityId: String)

    @Query("SELECT * FROM ${Constant.CURRENT} ORDER BY ${Constant.ADD_TIME}")
    fun loadAll(): Single<Array<CurrentEntity>>
}