package net.zackzhang.app.cold.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_city_search.*

import net.zackzhang.app.cold.R
import net.zackzhang.app.cold.model.bean.City

class CitySearchResultAdapter(private val citySearchList: List<City>) : BaseAdapter() {

    override fun getCount() = citySearchList.size

    override fun getItem(position: Int) = citySearchList[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val (_, cityName, prefName, provName) = getItem(position)
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_city_search, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.vCityNameText.text = cityName
        viewHolder.vPrefProvText.text = "$prefName, $provName"

        return view
    }

    class ViewHolder(override val containerView: View) : LayoutContainer
}
