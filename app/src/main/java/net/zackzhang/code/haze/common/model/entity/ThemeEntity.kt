package net.zackzhang.code.haze.common.model.entity

import android.os.Parcelable
import androidx.annotation.ColorInt
import kotlinx.parcelize.Parcelize

@Parcelize
data class ThemeEntity(
    @ColorInt val backgroundColor: Int,
    @ColorInt val foregroundColor: Int,
): Parcelable