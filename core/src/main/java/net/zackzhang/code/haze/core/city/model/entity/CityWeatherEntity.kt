package net.zackzhang.code.haze.core.city.model.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize
import net.zackzhang.code.haze.core.common.constant.ID
import net.zackzhang.code.haze.core.common.constant.NAME

@Parcelize
data class CityWeatherEntity(
    @ColumnInfo(name = ID)
    val id: String,
    @ColumnInfo(name = NAME)
    val name: String?,
): Parcelable