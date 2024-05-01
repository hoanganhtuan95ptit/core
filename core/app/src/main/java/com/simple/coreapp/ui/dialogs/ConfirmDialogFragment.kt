package com.simple.coreapp.ui.dialogs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.simple.coreapp.R
import com.simple.coreapp.databinding.DialogConfirmBinding
import com.simple.coreapp.ui.base.dialogs.BaseViewBindingDialogFragment
import com.simple.coreapp.utils.extentions.setDebouncedClickListener
import com.simple.coreapp.utils.extentions.setGone

class ConfirmDialogFragment : BaseViewBindingDialogFragment<DialogConfirmBinding>(R.layout.dialog_confirm) {

    var image: Int = 0
    var negativeListener: View.OnClickListener? = null
    var positiveListener: View.OnClickListener? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DialogConfirmBinding.bind(view)

        super.onViewCreated(view, savedInstanceState)

        binding!!.tvTitle.text = requireArguments().getString(TITLE)
        binding!!.tvMessage.text = requireArguments().getString(MESSAGE)

        binding!!.lottieAnimationView.setAnimation(image.takeIf { it > 0 } ?: R.raw.img_core_error)
        binding!!.lottieAnimationView.playAnimation()

        setUpPositive()
        setUpNegative()
    }

    private fun setUpPositive() {
        val text = requireArguments().getString(POSITIVE)

        binding!!.tvPositive.text = text
        binding!!.tvPositive.setGone(text.isNullOrBlank())

        binding!!.tvPositive.setDebouncedClickListener {
            positiveListener?.onClick(it)
            dismiss()
        }
    }

    private fun setUpNegative() {
        val text = requireArguments().getString(NEGATIVE)

        binding!!.tvNegative.text = text
        binding!!.tvNegative.setGone(text.isNullOrBlank())

        binding!!.tvNegative.setDebouncedClickListener {
            negativeListener?.onClick(it)
            dismiss()
        }
    }

    companion object {

        const val TITLE = "TITLE"
        const val MESSAGE = "MESSAGE"
        const val NEGATIVE = "NEGATIVE"
        const val POSITIVE = "POSITIVE"

        fun newInstance(
            fragmentManager: FragmentManager,

            title: String,
            message: String,
            negative: String = "",
            positive: String = "",
            cancelable: Boolean = true, image: Int = 0,

            negativeListener: View.OnClickListener? = null,
            positiveListener: View.OnClickListener? = null
        ) {
            val args = Bundle()
            args.putString(TITLE, title)
            args.putString(MESSAGE, message)
            args.putString(NEGATIVE, negative)
            args.putString(POSITIVE, positive)

            val fragment = ConfirmDialogFragment()

            fragment.image = image
            fragment.arguments = args
            fragment.isCancelable = cancelable
            fragment.negativeListener = negativeListener
            fragment.positiveListener = positiveListener

            fragment.show(fragmentManager, "")
        }
    }
}
