package me.imzack.app.cold.view.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import me.imzack.app.cold.model.DataManager

import me.imzack.app.cold.view.fragment.WeatherPageFragment

class WeatherPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = WeatherPageFragment.newInstance(position)

    override fun getCount() = DataManager.cityCount

    override fun getItemPosition(obj: Any) = if ((obj as WeatherPageFragment).isCityDeleted) PagerAdapter.POSITION_NONE else super.getItemPosition(obj)

    override fun getPageTitle(position: Int) = DataManager.getWeather(position).basic.cityName
}
