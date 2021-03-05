package net.zackzhang.code.haze.city.model.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize
import net.zackzhang.code.haze.common.Constants

@Parcelize
data class CityWeatherEntity(
    @ColumnInfo(name = Constants.ID)
    val id: String,
    @ColumnInfo(name = Constants.NAME)
    val name: String?,
): Parcelable