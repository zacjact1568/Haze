package net.zackzhang.app.cold.view.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_cities.*
import net.zackzhang.app.cold.R
import net.zackzhang.app.cold.presenter.CitiesPresenter
import net.zackzhang.app.cold.view.adapter.CityAdapter
import net.zackzhang.app.cold.view.contract.CitiesViewContract
import net.zackzhang.app.cold.view.dialog.MessageDialogFragment

class CitiesFragment : BaseFragment(), CitiesViewContract {

    companion object {

        private const val TAG_CITY_DELETION = "city_deletion"

        private const val KEY_CITY_DELETION_POSITION = "city_deletion_position"
    }

    private val citiesPresenter = CitiesPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_cities, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        citiesPresenter.attach()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        if (childFragment.tag == TAG_CITY_DELETION) {
            val dialogFragment = childFragment as MessageDialogFragment
            dialogFragment.okButtonClickListener = {
                citiesPresenter.notifyCityDeleted(dialogFragment.arguments!!.getInt(KEY_CITY_DELETION_POSITION))
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        citiesPresenter.detach()
    }

    override fun showInitialView(cityAdapter: CityAdapter) {
        vCityList.setHasFixedSize(true)
        vCityList.adapter = cityAdapter
    }

    override fun showCityDeletionConfirmationDialog(cityName: String, position: Int) {
        val dialogFragment = MessageDialogFragment.Builder()
                .setTitle(R.string.title_dialog_city_deletion_confirmation)
                .setMessage(String.format(getString(R.string.msg_dialog_city_deletion_confirmation), cityName))
                .setOkButtonText(R.string.pos_btn_city_deletion_confirmation)
                .showCancelButton()
                .build()
        // 将要删除城市的 position 存入 MessageDialogFragment 的 arguments
        // 只要此 DialogFragment 未手动关闭，就一定能取到这个值
        dialogFragment.arguments!!.putInt(KEY_CITY_DELETION_POSITION, position)
        dialogFragment.show(childFragmentManager, TAG_CITY_DELETION)
    }
}
