package net.zackzhang.code.haze.core.city.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import net.zackzhang.code.haze.core.city.model.entity.CityEntity
import net.zackzhang.code.haze.core.common.constant.CITY
import net.zackzhang.code.haze.core.common.constant.ID

@Dao
interface CityDao {

    @Insert
    suspend fun insert(entity: CityEntity)

    // 有外键约束，会同时删除 weather 和 air 表中对应城市的列
    @Query("DELETE FROM $CITY WHERE $ID = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM $CITY")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceBy(entity: CityEntity) {
        deleteAll()
        insert(entity)
    }
}