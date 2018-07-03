package net.zackzhang.app.cold.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import net.zackzhang.app.cold.App
import net.zackzhang.app.cold.R
import net.zackzhang.app.cold.common.Constant
import net.zackzhang.app.cold.event.CityAddedEvent
import net.zackzhang.app.cold.model.DataManager
import net.zackzhang.app.cold.model.bean.Weather
import net.zackzhang.app.cold.util.ResourceUtil
import net.zackzhang.app.cold.util.StringUtil
import me.imzack.lib.baseguideactivity.SimpleGuidePageFragment

class LocationGuidePageFragment : SimpleGuidePageFragment() {

    companion object {

        fun newInstance(): LocationGuidePageFragment {
            val fragment = LocationGuidePageFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isLocationServiceEnabled = LocationServicePermissionsFragment.locationServiceEnabled

        imageResId = R.drawable.ic_place_black_24dp
        imageTint = Color.WHITE
        titleText = StringUtil.addWhiteColorSpan(ResourceUtil.getString(R.string.title_page_location))
        descriptionText = StringUtil.addWhiteColorSpan(ResourceUtil.getString(if (isLocationServiceEnabled) R.string.description_page_location_enabled else R.string.description_page_location_disabled))
        buttonText = if (isLocationServiceEnabled) null else StringUtil.addWhiteColorSpan(ResourceUtil.getString(R.string.btn_page_location))
        buttonBackgroundColor = ResourceUtil.getColor(R.color.colorAccent)
        buttonClickListener = {
            var lspFragment = childFragmentManager.findFragmentByTag(LocationServicePermissionsFragment.TAG_LOCATION_SERVICE_PERMISSIONS) as LocationServicePermissionsFragment?
            if (lspFragment == null) {
                lspFragment = LocationServicePermissionsFragment()
                childFragmentManager.beginTransaction().add(lspFragment, LocationServicePermissionsFragment.TAG_LOCATION_SERVICE_PERMISSIONS).commitNow()
            }
            lspFragment.requestPermissions()
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        if (childFragment.tag == LocationServicePermissionsFragment.TAG_LOCATION_SERVICE_PERMISSIONS) {
            (childFragment as LocationServicePermissionsFragment).permissionsRequestFinishedListener = {
                if (it) {
                    // 如果成功授权
                    // 开启位置服务
                    DataManager.preferenceHelper.locationServiceValue = true
                    // 更新界面
                    descriptionText = StringUtil.addWhiteColorSpan(getString(R.string.description_page_location_enabled))
                    buttonText = null
                    // TODO 不用（放到 HomeActivity 中，不然中途退出引导又进入会重复存入数据库）因为后续“当前位置”只与preferenceHelper.locationServiceValue有关，而这个选项是无法在app外部修改的
                    // 先添加“当前位置”占位，待后续获取到位置再更新
                    // 在引导页添加的城市位置肯定是 0
                    DataManager.notifyCityAdded(Weather.City(Constant.CITY_ID_CURRENT_LOCATION, getString(R.string.text_current_location)))
                    App.eventBus.post(CityAddedEvent(
                            javaClass.simpleName,
                            Constant.CITY_ID_CURRENT_LOCATION,
                            DataManager.recentlyAddedCityLocation
                    ))
                }
                // 如果授权被拒绝，不做任何操作
            }
        }
    }
}