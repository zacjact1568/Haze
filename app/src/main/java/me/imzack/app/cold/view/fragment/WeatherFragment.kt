package me.imzack.app.cold.view.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_weather.*
import me.imzack.app.cold.R
import me.imzack.app.cold.presenter.WeatherPresenter
import me.imzack.app.cold.view.activity.CityAddActivity
import me.imzack.app.cold.view.adapter.WeatherPagerAdapter
import me.imzack.app.cold.view.contract.WeatherViewContract

class WeatherFragment : BaseFragment(), WeatherViewContract {

    // 需要延迟初始化，否则一开始此 fragment 还未附到 activity 上，无法获取 fragmentManager
    private val weatherPresenter by lazy { WeatherPresenter(this, fragmentManager!!) }

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
        vAddCityButton.setOnClickListener { CityAddActivity.start(context!!) }

        onPageEmptyStateChanged(isCityEmpty)
    }

    override fun onDetectedNetworkNotAvailable() {
        Snackbar.make(vWeatherPager, R.string.text_network_not_available, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_network_settings) { startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS)) }
                .show()
    }

    override fun onSwitchPage(position: Int) {
        vWeatherPager.currentItem = position
    }

    override fun onPageEmptyStateChanged(isEmpty: Boolean) {
        vWeatherPager.visibility = if (isEmpty) View.GONE else View.VISIBLE
        vEmptyLayout.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}