package me.imzack.app.cold.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_city.*

import me.imzack.app.cold.R
import me.imzack.app.cold.common.Constant
import me.imzack.app.cold.model.DataManager
import me.imzack.app.cold.model.bean.Weather
import me.imzack.app.cold.util.ResourceUtil

class CityAdapter : RecyclerView.Adapter<CityAdapter.ItemViewHolder>() {

    var onCityItemClickListener: ((position: Int) -> Unit)? = null
    var onUpdateButtonClickListener: ((position: Int) -> Unit)? = null
    var onDeleteButtonClickListener: ((position: Int) -> Unit)? = null

    private val updateAnim = ResourceUtil.getAnimation(R.anim.anim_update)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_city, parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val (_, cityName, current, _, _, updateTime, status) = DataManager.getWeather(position)

        holder.vCityNameText.text = cityName
        holder.vWeatherText.text = if (updateTime == 0L) "${Constant.UNKNOWN_DATA} | ${Constant.UNKNOWN_DATA}" else "${current.temperature} | ${DataManager.getConditionByCode(current.conditionCode)}"

        if (status == Weather.STATUS_ON_UPDATING) {
            holder.vUpdateButton.startAnimation(updateAnim)
        }

        holder.itemView.setOnClickListener { onCityItemClickListener?.invoke(holder.layoutPosition) }
        holder.vUpdateButton.setOnClickListener { onUpdateButtonClickListener?.invoke(holder.layoutPosition) }
        holder.vDeleteButton.setOnClickListener { onDeleteButtonClickListener?.invoke(holder.layoutPosition) }
    }

    override fun getItemCount() = DataManager.cityCount

    class ItemViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}
