package net.zackzhang.code.haze.view.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import net.zackzhang.code.haze.model.DataManager
import net.zackzhang.code.haze.view.fragment.WeatherPageFragment

class WeatherPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = WeatherPageFragment.newInstance(position)

    override fun getCount() = DataManager.cityCount

    // 有城市的添加和删除，直接让所有已实例化的城市销毁重建，选择性更新会出问题
    // 实际上开销也不大，因为最多只会实例化三个城市
    override fun getItemPosition(obj: Any) = POSITION_NONE

    override fun getPageTitle(position: Int) = DataManager.getWeather(position).cityName
}
