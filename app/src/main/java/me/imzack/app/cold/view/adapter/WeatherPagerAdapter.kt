package me.imzack.app.cold.view.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import me.imzack.app.cold.model.DataManager
import me.imzack.app.cold.view.fragment.WeatherPageFragment

class WeatherPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = WeatherPageFragment.newInstance(position)

    override fun getCount() = DataManager.cityCount

    override fun getItemPosition(obj: Any): Int {
        val fragment = obj as WeatherPageFragment
        return when {
            fragment.isPositionChanged -> {
                // 在这里又将 isPositionChanged 属性置为 false
                // PS：实在找不到更好的解决办法了
                fragment.isPositionChanged = false
                fragment.position
            }
            fragment.isCityDeleted -> POSITION_NONE
            else -> POSITION_UNCHANGED
        }
    }

    override fun getPageTitle(position: Int) = DataManager.getWeather(position).cityName
}
