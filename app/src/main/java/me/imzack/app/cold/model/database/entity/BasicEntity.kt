package me.imzack.app.cold.model.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import me.imzack.app.cold.common.Constant

@Entity(tableName = Constant.BASIC)
data class BasicEntity(
        @PrimaryKey
        @ColumnInfo(name = Constant.CITY_ID)
        val cityId: String,
        @ColumnInfo(name = Constant.CITY_NAME)
        val cityName: String,
        @ColumnInfo(name = Constant.LONGITUDE)
        val longitude: Double,
        @ColumnInfo(name = Constant.LATITUDE)
        val latitude: Double,
        @ColumnInfo(name = Constant.UPDATE_TIME)
        val updateTime: Long
)