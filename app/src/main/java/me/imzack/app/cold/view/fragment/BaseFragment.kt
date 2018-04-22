package me.imzack.app.cold.view.fragment

import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.widget.Toast

abstract class BaseFragment : Fragment() {

    protected val fragmentName
        get() = javaClass.simpleName!!

    fun showToast(@StringRes msgResId: Int) {
        Toast.makeText(context, msgResId, Toast.LENGTH_SHORT).show()
    }

    open fun exit() {
        // 如果此 Fragment 附在某个 Activity 上，fragmentManager 是 Activity 的 fragmentManager
        // 如果此 Fragment 作为子 Fragment 附在某个父 Fragment 上，fragmentManager 是父 Fragment 的 childFragmentManager
        // 总之，该语句的确可以将此 Fragment 从宿主类中移除（异步）
        fragmentManager!!.beginTransaction().remove(this).commit()
    }
}
