package net.zackzhang.code.haze.view.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_weather.*
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.presenter.WeatherPresenter
import net.zackzhang.code.haze.view.adapter.WeatherPagerAdapter
import net.zackzhang.code.haze.view.contract.WeatherViewContract

class WeatherFragment : BaseFragment(), WeatherViewContract {

    // 需要延迟初始化，否则一开始此 fragment 还未附到 activity 上，无法获取 fragmentManager
    private val weatherPresenter by lazy { WeatherPresenter(this, fragmentManager!!) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            // 先直接添加 EmptyCityFragment，后续改变其可见性即可
            childFragmentManager.beginTransaction().add(R.id.vEmptyCityLayout, EmptyCityFragment()).commit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_weather, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherPresenter.attach()
    }

    override fun onDetach() {
        super.onDetach()
        weatherPresenter.detach()
    }

    override fun showInitialView(weatherPagerAdapter: WeatherPagerAdapter, isCityEmpty: Boolean) {
        vWeatherPager.adapter = weatherPagerAdapter

        onCityEmptyStateChanged(isCityEmpty)
    }

    override fun onDetectedNetworkNotAvailable() {
        Snackbar.make(vWeatherPager, R.string.text_network_not_available, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_settings) { startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS)) }
                .show()
    }

    override fun onSwitchPage(position: Int) {
        vWeatherPager.currentItem = position
    }

    override fun onCityEmptyStateChanged(isEmpty: Boolean) {
        vWeatherPager.visibility = if (isEmpty) View.GONE else View.VISIBLE
        // 通过改变容器 view 的可见性来改变 EmptyCityFragment 的可见性
        vEmptyCityLayout.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}