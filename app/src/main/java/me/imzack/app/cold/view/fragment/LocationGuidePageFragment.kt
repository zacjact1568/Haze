package me.imzack.app.cold.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import me.imzack.app.cold.R
import me.imzack.app.cold.util.ResourceUtil
import me.imzack.app.cold.util.StringUtil
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
                    // 如果成功授权，更新界面
                    descriptionText = StringUtil.addWhiteColorSpan(getString(R.string.description_page_location_enabled))
                    buttonText = null
                }
                // 如果授权被拒绝，不做任何操作
            }
        }
    }
}