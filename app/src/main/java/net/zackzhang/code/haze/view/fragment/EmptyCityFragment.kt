package net.zackzhang.code.haze.view.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_empty_city.*
import net.zackzhang.code.haze.App
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.common.Constant
import net.zackzhang.code.haze.event.CityAddedEvent
import net.zackzhang.code.haze.model.DataManager
import net.zackzhang.code.haze.model.bean.Weather
import net.zackzhang.code.haze.view.activity.CityAddActivity
import net.zackzhang.code.haze.view.dialog.PreEnableLocationServiceDialogFragment

class EmptyCityFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_empty_city, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vLocationServiceButton.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                enableLocationService()
            } else {
                PreEnableLocationServiceDialogFragment().show(childFragmentManager)
            }
        }
        vSelectCityButton.setOnClickListener { CityAddActivity.start(context!!) }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        if (childFragment.tag == PreEnableLocationServiceDialogFragment.TAG_PRE_ENABLE_LOCATION_SERVICE) {
            (childFragment as PreEnableLocationServiceDialogFragment).okButtonClickListener = { enableLocationService() }
        }
    }

    private fun enableLocationService() {
        DataManager.preferenceHelper.locationServiceValue = true
        DataManager.notifyAddingCity(Weather(Constant.CITY_ID_CURRENT_LOCATION, getString(R.string.text_current_location), isLocationCity = true))
        App.eventBus.post(CityAddedEvent(
                javaClass.simpleName,
                Constant.CITY_ID_CURRENT_LOCATION,
                0
        ))
        // 会调用 WeatherPresenter 的 onCityAdded 订阅函数 TODO 其他类都改成这样（删除 eventSource）？
    }
}