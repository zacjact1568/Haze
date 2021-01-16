package net.zackzhang.code.haze.view.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import net.zackzhang.code.haze.App
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.Constant
import net.zackzhang.code.haze.event.CityAddedEvent
import net.zackzhang.code.haze.model.DataManager
import net.zackzhang.code.haze.model.bean.Weather
import net.zackzhang.code.haze.util.ResourceUtil
import net.zackzhang.code.haze.util.StringUtil
import net.zackzhang.lib.baseguideactivity.SimpleGuidePageFragment
import net.zackzhang.code.haze.view.dialog.PreEnableLocationServiceDialogFragment

class LocationGuidePageFragment : SimpleGuidePageFragment() {

    private val preferenceHelper = DataManager.preferenceHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isLocationServiceEnabled = preferenceHelper.locationServiceValue

        imageResId = R.drawable.ic_location
        imageTint = Color.WHITE
        titleText = StringUtil.addWhiteColorSpan(ResourceUtil.getString(R.string.title_page_location))
        descriptionText = StringUtil.addWhiteColorSpan(ResourceUtil.getString(if (isLocationServiceEnabled) R.string.description_page_location_enabled else R.string.description_page_location_disabled))
        buttonText = if (isLocationServiceEnabled) null else StringUtil.addWhiteColorSpan(ResourceUtil.getString(R.string.btn_page_location))
        buttonBackgroundColor = ResourceUtil.getColor(R.color.colorAccent)
        buttonClickListener = {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                // Android 6.0 以下，直接开启位置服务，因为不需要授权
                enableLocationService()
            } else {
                PreEnableLocationServiceDialogFragment().show(childFragmentManager)
            }
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        if (childFragment.tag == PreEnableLocationServiceDialogFragment.TAG_PRE_ENABLE_LOCATION_SERVICE) {
            (childFragment as PreEnableLocationServiceDialogFragment).okButtonClickListener = { enableLocationService() }
        }
    }

    private fun enableLocationService() {
        // 开启位置服务
        preferenceHelper.locationServiceValue = true
        // 更新界面
        descriptionText = StringUtil.addWhiteColorSpan(getString(R.string.description_page_location_enabled))
        buttonText = null
        // 先添加“当前位置”占位，待后续获取到位置再更新
        // 在引导页添加的城市位置肯定是 0，因此不需要指定插入位置（notifyAddingCity 默认在尾部插入）
        // TODO 定位到的城市都是县级？
        DataManager.notifyAddingCity(Weather(Constant.CITY_ID_CURRENT_LOCATION, getString(R.string.text_current_location), isLocationCity = true))
        App.eventBus.post(CityAddedEvent(
                javaClass.simpleName,
                Constant.CITY_ID_CURRENT_LOCATION,
                0
        ))
    }
}