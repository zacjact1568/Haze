package net.zackzhang.app.cold.view.dialog

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.View
import kotlinx.android.synthetic.main.dialog_fragment_message.*
import net.zackzhang.app.cold.R

class PreEnableLocationServiceDialogFragment : MessageDialogFragment() {

    companion object {

        const val TAG_PRE_ENABLE_LOCATION_SERVICE = "pre_enable_location_service"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 没有使用 arguments，因为这些文本不是通过外部传入的，而是写死在类中的，重建后一定能恢复

        titleText = getString(R.string.title_dialog_pre_enable_location_service)

        vMessageText.text = getString(R.string.msg_dialog_pre_enable_location_service)

        positiveButtonText = getString(R.string.pos_btn_dialog_pre_enable_location_service)

        negativeButtonText = getString(android.R.string.cancel)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG_PRE_ENABLE_LOCATION_SERVICE)
    }
}