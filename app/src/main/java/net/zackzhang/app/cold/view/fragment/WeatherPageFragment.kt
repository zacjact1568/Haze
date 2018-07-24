package net.zackzhang.app.cold.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_weather_page.*
import net.zackzhang.app.cold.R
import net.zackzhang.app.cold.common.Constant
import net.zackzhang.app.cold.model.bean.FormattedWeather
import net.zackzhang.app.cold.presenter.WeatherPagePresenter
import net.zackzhang.app.cold.view.contract.WeatherPageViewContract

class WeatherPageFragment : BaseFragment(), WeatherPageViewContract {

    companion object {

        fun newInstance(weatherListPosition: Int): WeatherPageFragment {
            val fragment = WeatherPageFragment()
            val args = Bundle()
            args.putInt(Constant.WEATHER_LIST_POSITION, weatherListPosition)
            fragment.arguments = args
            return fragment
        }
    }

    private val weatherPagePresenter by lazy {
        // 没必要更新 position 了，因为有城市添加或删除，所有 page 会销毁重建，position 会自动变成新的
        WeatherPagePresenter(this, arguments!!.getInt(Constant.WEATHER_LIST_POSITION))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_weather_page, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherPagePresenter.attach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        weatherPagePresenter.notifyStartingUpCompleted()
    }

    override fun onDetach() {
        super.onDetach()
        weatherPagePresenter.detach()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        // 在不同 page 之间切换时调用
    }

    override fun showInitialView(formattedWeather: FormattedWeather) {
        vSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
        vSwipeRefreshLayout.setOnRefreshListener { weatherPagePresenter.notifyUpdatingWeather() }
        showWeatherView(formattedWeather)
    }

    override fun showWeatherView(formattedWeather: FormattedWeather) {
        changeSwipeRefreshingStatus(formattedWeather.isUpdating)

        changeCityName(formattedWeather.cityName)
        vTemperatureText.text = formattedWeather.temperature
        vConditionText.text = formattedWeather.condition
        vUpdateTimeText.text = formattedWeather.updateTime

        vFeelsLikeText.text = formattedWeather.feelsLike
        vTemperatureRangeText.text = formattedWeather.tempRange
        vAirQualityText.text = formattedWeather.airQuality

        vTemperatureTrendChart.setTemperatureData(formattedWeather.weeks, formattedWeather.conditions, formattedWeather.maxTemps, formattedWeather.minTemps)
    }

    override fun changeSwipeRefreshingStatus(isRefreshing: Boolean) {
        vSwipeRefreshLayout.isRefreshing = isRefreshing
    }

    override fun changeCityName(cityName: String) {
        vCityNameText.text = cityName
    }
}
