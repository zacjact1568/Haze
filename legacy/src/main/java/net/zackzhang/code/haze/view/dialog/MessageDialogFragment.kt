package net.zackzhang.code.haze.view.dialog

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_fragment_message.*
import net.zackzhang.code.haze.R
import net.zackzhang.code.haze.util.ResourceUtil
import net.zackzhang.lib.basedialogfragment.BaseDialogFragment

/**
 * 显示一段文本信息的 dialog，左下一个按钮（third），右下两个按钮（cancel & ok），
 * 可控制 cancel 按钮显示与否，但其文本不可更改。
 * 无论是否为按钮设置监听事件，触摸按钮后都关闭 dialog。
 */
open class MessageDialogFragment : BaseDialogFragment() {

    companion object {

        private const val ARG_MESSAGE = "message"
    }

    var okButtonClickListener: (() -> Unit)? = null

    var thirdButtonClickListener: (() -> Unit)? = null

    override fun onCreateContentView(inflater: LayoutInflater, root: ViewGroup) =
            inflater.inflate(R.layout.dialog_fragment_message, root, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vMessageText.text = arguments?.getCharSequence(ARG_MESSAGE)

        neutralButtonClickListener = {
            thirdButtonClickListener?.invoke()
            true
        }
        // 使触摸 negative 按钮回调 onCancel 而不是 onDismiss
        negativeButtonClickListener = {
            // DialogFragment 中的 dismiss 除了调用 dialog 的 dismiss，还进行了一些其他的处理
            // 但是 DialogFragment 中没有 cancel 函数，可以直接调用 dialog 的 cancel，也会进行相同的处理
            // 因为调用 dialog 的 cancel 也会调用 dismiss，就会调用 DialogFragment 的 onDismiss 函数
            // 在 DialogFragment 的 onDismiss 函数中就会调用 dismissInternal 函数进行处理
            dialog!!.cancel()
            // 返回 false，不调用 dismiss
            false
        }
        positiveButtonClickListener = {
            okButtonClickListener?.invoke()
            true
        }
    }

    class Builder {

        private val args = Bundle()

        fun setTitle(title: CharSequence): Builder {
            putTitle(args, title)
            return this
        }

        fun setTitle(@StringRes titleResId: Int) = setTitle(ResourceUtil.getString(titleResId))

        fun setMessage(message: CharSequence): Builder {
            args.putCharSequence(ARG_MESSAGE, message)
            return this
        }

        fun setMessage(@StringRes messageResId: Int) = setMessage(ResourceUtil.getString(messageResId))

        fun setOkButtonText(okButtonText: CharSequence): Builder {
            putPositiveButtonText(args, okButtonText)
            return this
        }

        fun setOkButtonText(@StringRes okButtonTextResId: Int) = setOkButtonText(ResourceUtil.getString(okButtonTextResId))

        fun setThirdButtonText(thirdButtonText: CharSequence): Builder {
            putNeutralButtonText(args, thirdButtonText)
            return this
        }

        fun setThirdButtonText(@StringRes thirdButtonTextResId: Int) = setThirdButtonText(ResourceUtil.getString(thirdButtonTextResId))

        fun showCancelButton(): Builder {
            putNegativeButtonText(args, ResourceUtil.getString(android.R.string.cancel))
            return this
        }

        fun build(): MessageDialogFragment {
            val fragment = MessageDialogFragment()
            fragment.arguments = args
            return fragment
        }

        fun show(fm: FragmentManager, tag: String? = null) {
            build().show(fm, tag)
        }
    }
}