package com.simple.coreapp.ui.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.simple.core.utils.extentions.orZero
import com.simple.coreapp.Param
import com.simple.coreapp.databinding.DialogConfirmHorizontalBinding
import com.simple.coreapp.databinding.DialogConfirmVerticalBinding
import com.simple.coreapp.ui.base.dialogs.sheet.BaseViewBindingSheetFragment
import com.simple.coreapp.utils.ext.setDebouncedClickListener
import com.simple.coreapp.utils.ext.setVisible

class HorizontalConfirmDialogFragment : BaseViewBindingSheetFragment<DialogConfirmHorizontalBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = binding ?: return

        val isCancelable = arguments?.getBoolean(Param.PARAM_CANCEL) == true
        dialog?.setCancelable(isCancelable)
        dialog?.setCanceledOnTouchOutside(isCancelable)

        val title = arguments?.getString(Param.PARAM_TITLE)
        binding.tvTitle.text = title
        binding.tvTitle.setVisible(!title.isNullOrBlank())

        val image = arguments?.getInt(Param.PARAM_IMAGE).orZero()
        binding.ivLogo.setImageResource(image)
        binding.ivLogo.setVisible(image != 0)

        val message = arguments?.getString(Param.PARAM_MESSAGE)
        binding.tvMessage.text = message
        binding.tvMessage.setVisible(!message.isNullOrBlank())

        val negative = arguments?.getString(Param.PARAM_NEGATIVE)
        binding.tvNegative.setVisible(!negative.isNullOrBlank())
        binding.tvNegative.text = negative
        binding.tvNegative.setDebouncedClickListener {
            setFragmentResult(arguments?.getString(Param.KEY_REQUEST_NEGATIVE).orEmpty(), bundleOf(Param.RESULT_CODE to 1))
            dismiss()
        }

        val positive = arguments?.getString(Param.PARAM_POSITIVE)
        binding.tvPositive.setVisible(!positive.isNullOrBlank())
        binding.tvPositive.text = positive
        binding.tvPositive.setDebouncedClickListener {
            setFragmentResult(arguments?.getString(Param.KEY_REQUEST_POSITIVE).orEmpty(), bundleOf(Param.RESULT_CODE to 1))
            dismiss()
        }
    }

    companion object {

        fun newInstance(
            isCancel: Boolean = true,
            keyRequestPositive: String? = null,
            keyRequestNegative: String? = null,
            image: Int? = null,
            title: String? = null,
            message: String? = null,
            negative: String? = null,
            positive: String? = null
        ) = HorizontalConfirmDialogFragment().apply {

            arguments = bundleOf(
                Param.PARAM_IMAGE to image,
                Param.PARAM_CANCEL to isCancel,
                Param.KEY_REQUEST_POSITIVE to keyRequestPositive,
                Param.KEY_REQUEST_NEGATIVE to keyRequestNegative,
                Param.PARAM_TITLE to title,
                Param.PARAM_MESSAGE to message,
                Param.PARAM_NEGATIVE to negative,
                Param.PARAM_POSITIVE to positive
            )
        }
    }
}