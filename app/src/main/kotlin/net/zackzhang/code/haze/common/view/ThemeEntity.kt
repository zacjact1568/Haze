package net.zackzhang.code.haze.common.view

import android.os.Parcelable
import androidx.annotation.ColorRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class ThemeEntity(
    // ThemeEntity 会保存在 ViewModel 中，所以这里需要是与深浅色模式无关的资源 ID
    // 当深浅色模式切换后，重建时再去取对应的颜色
    @param:ColorRes val accentColor: Int,
): Parcelable