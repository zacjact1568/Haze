package net.zackzhang.code.haze.components.recycler.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.zackzhang.code.haze.models.ThemeEntity
import net.zackzhang.code.haze.utils.ItemDecorationRectInsets

abstract class CardBase(
    parent: ViewGroup,
    resId: Int,
    onClick: ((position: Int) -> Unit)? = null,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(resId, parent, false),
) {

    protected val context = parent.context!!

    init {
        onClick?.let {
            itemView.setOnClickListener { _ ->
                it(layoutPosition)
            }
        }
    }

    abstract fun updateViews(config: CardConfigBase)
}

abstract class CardConfigBase(
    val type: CardType,
    var theme: ThemeEntity? = null,
    /**
     * 一个卡片的网格跨度
     */
    val spanSize: Int? = null,
    val decorationRectInsets: ItemDecorationRectInsets? = null,
    val needBackground: Boolean = true,
)

enum class CardType {
    WEATHER_HEAD,
    WEATHER_HOURLY_ROW,
    WEATHER_TITLE,
    WEATHER_DAILY,
    WEATHER_CURRENT,
    CITY_SEARCH_ASSOCIATION,
    WEATHER_HOURLY,
    SETTINGS_SWITCH_PREFERENCE,
    SETTINGS_INFO_PREFERENCE,
    SOURCE,
    SPACE,
}