package com.one.coreapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.one.coreapp.R
import com.one.coreapp.databinding.DialogConfirmBinding
import com.one.coreapp.ui.base.fragments.BaseSheetFragment
import com.one.coreapp.utils.extentions.setDebouncedClickListener
import com.one.coreapp.utils.extentions.setGone

class ConfirmDialogFragment : BaseSheetFragment() {

    companion object {

        const val TITLE = "TITLE"
        const val MESSAGE = "MESSAGE"
        const val NEGATIVE = "NEGATIVE"
        const val POSITIVE = "POSITIVE"

        fun newInstance(
            fragmentManager: FragmentManager,

            title: String, message: String,

            negative: String = "", positive: String = "",

            cancelable: Boolean = true, image: Int = 0,

            negativeListener: View.OnClickListener? = null, positiveListener: View.OnClickListener? = null
        ) {
            val args = Bundle()
            args.putString(TITLE, title)
            args.putString(MESSAGE, message)
            args.putString(NEGATIVE, negative)
            args.putString(POSITIVE, positive)

            val fragment = ConfirmDialogFragment()

            fragment.image = image
            fragment.negativeListener = negativeListener
            fragment.positiveListener = positiveListener
            fragment.arguments = args

            fragment.isCancelable = cancelable

            fragment.show(fragmentManager, "")
        }
    }

    var image: Int = 0

    var negativeListener: View.OnClickListener? = null

    var positiveListener: View.OnClickListener? = null

    private var binding: DialogConfirmBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.cloneInContext(requireContext()).inflate(R.layout.dialog_confirm, container, false)
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
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
}
