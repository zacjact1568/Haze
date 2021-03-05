package net.zackzhang.code.haze.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_city.*
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.Constant
import net.zackzhang.code.haze.model.DataManager
import net.zackzhang.code.haze.model.bean.Weather
import net.zackzhang.code.haze.util.ResourceUtil

class CityAdapter : RecyclerView.Adapter<CityAdapter.ItemViewHolder>() {

    var onCityItemClickListener: ((position: Int) -> Unit)? = null
    var onUpdateButtonClickListener: ((position: Int) -> Unit)? = null
    var onDeleteButtonClickListener: ((position: Int) -> Unit)? = null

    private val updateAnim = ResourceUtil.getAnimation(R.anim.anim_update)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_city, parent, false))

    // TODO use payloads
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val (city, current, _, _, updateTime, _, status) = DataManager.getWeather(position)

        holder.vCityNameText.text = city.name
        holder.vWeatherText.text = if (updateTime == 0L) "${Constant.UNKNOWN_DATA} | ${Constant.UNKNOWN_DATA}" else "${current.temperature} | ${DataManager.getConditionByCode(current.conditionCode)}"

        if (status == Weather.STATUS_UPDATING) {
            holder.vUpdateButton.startAnimation(updateAnim)
        }

        if (DataManager.isLocationCity(position)) {
            holder.vCityNameText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_location, 0)
            holder.vDeleteButton.visibility = View.INVISIBLE
        }

        holder.itemView.setOnClickListener { onCityItemClickListener?.invoke(holder.layoutPosition) }
        holder.vUpdateButton.setOnClickListener { onUpdateButtonClickListener?.invoke(holder.layoutPosition) }
        holder.vDeleteButton.setOnClickListener { onDeleteButtonClickListener?.invoke(holder.layoutPosition) }
    }

    override fun getItemCount() = DataManager.cityCount

    class ItemViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}
