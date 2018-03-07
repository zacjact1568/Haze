package me.imzack.app.cold.view.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_cities.*
import me.imzack.app.cold.R
import me.imzack.app.cold.presenter.CitiesPresenter
import me.imzack.app.cold.view.adapter.CityAdapter
import me.imzack.app.cold.view.contract.CitiesViewContract
import me.imzack.app.cold.view.dialog.MessageDialogFragment

class CitiesFragment : BaseFragment(), CitiesViewContract {

    private val citiesPresenter = CitiesPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_cities, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        citiesPresenter.attach()
    }

    override fun onDetach() {
        super.onDetach()
        citiesPresenter.detach()
    }

    override fun showInitialView(cityAdapter: CityAdapter) {
        vCityList.setHasFixedSize(true)
        vCityList.adapter = cityAdapter
    }

    override fun onDetectedNetworkNotAvailable() {
        Snackbar.make(vCityList, R.string.text_network_not_available, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_network_settings) { startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS)) }
                .show()
    }

    override fun showCityDeletionAlertDialog(cityName: String, position: Int) {
        MessageDialogFragment.newInstance(
                getString(R.string.msg_dialog_delete_city_confirm_head) + cityName + getString(R.string.msg_dialog_delete_city_confirm_tail),
                getString(R.string.title_dialog_delete_city_confirm),
                getString(R.string.btn_delete),
                { citiesPresenter.notifyCityDeleted(position) }
        ).show(fragmentManager!!)
    }
}
