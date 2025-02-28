package com.simple.coreapp.ui.dialogs.confirm

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.simple.core.utils.extentions.orZero
import com.simple.coreapp.Param
import com.simple.coreapp.databinding.DialogConfirmHorizontalBinding
import com.simple.coreapp.ui.base.dialogs.sheet.BaseViewBindingSheetFragment
import com.simple.coreapp.ui.view.Background
import com.simple.coreapp.ui.view.setBackground
import com.simple.coreapp.utils.ext.ButtonInfo
import com.simple.coreapp.utils.ext.doOnHeightStatusAndHeightNavigationChange
import com.simple.coreapp.utils.ext.getParcelableOrNull
import com.simple.coreapp.utils.ext.setDebouncedClickListener
import com.simple.coreapp.utils.ext.setVisible

class HorizontalConfirmDialogFragment : BaseViewBindingSheetFragment<DialogConfirmHorizontalBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doOnHeightStatusAndHeightNavigationChange { heightStatusBar, heightNavigationBar ->

            binding?.root?.setPadding(0, 0, 0, heightNavigationBar)
        }

        val binding = binding ?: return

        val isCancelable = arguments?.getBoolean(Param.CANCEL) == true
        dialog?.setCancelable(isCancelable)
        dialog?.setCanceledOnTouchOutside(isCancelable)

        val anim = arguments?.getInt(Param.ANIM).orZero()
        if (anim != 0) binding.ivLogo.setAnimation(anim)

        val image = arguments?.getInt(Param.IMAGE).orZero()
        if (image != 0) binding.ivLogo.setImageResource(image)

        binding.ivLogo.setVisible(anim != 0 || image != 0)

        val title = arguments?.getCharSequence(Param.TITLE)
        binding.tvTitle.text = title
        binding.tvTitle.setVisible(!title.isNullOrBlank())

        val message = arguments?.getCharSequence(Param.MESSAGE)
        binding.tvMessage.text = message
        binding.tvMessage.setVisible(!message.isNullOrBlank())

        val anchor = arguments?.getParcelableOrNull<Background>(Param.ANCHOR)
        binding.anchor.delegate.setBackground(anchor)

        val background = arguments?.getParcelableOrNull<Background>(Param.BACKGROUND)
        binding.root.delegate.setBackground(background)

        val negative = arguments?.getParcelableOrNull<ButtonInfo>(Param.NEGATIVE)
        binding.tvNegative.setVisible(negative != null)
        binding.tvNegative.text = negative?.text
        binding.tvNegative.delegate.setBackground(negative?.background)
        binding.tvNegative.setDebouncedClickListener {
            setFragmentResult(arguments?.getString(Param.KEY_REQUEST).orEmpty(), bundleOf(Param.RESULT_CODE to 0))
            dismiss()
        }

        val positive = arguments?.getParcelableOrNull<ButtonInfo>(Param.POSITIVE)
        binding.tvPositive.setVisible(positive != null)
        binding.tvPositive.text = positive?.text
        binding.tvPositive.delegate.setBackground(positive?.background)
        binding.tvPositive.setDebouncedClickListener {
            setFragmentResult(arguments?.getString(Param.KEY_REQUEST).orEmpty(), bundleOf(Param.RESULT_CODE to 1))
            dismiss()
        }
    }

    companion object {

        fun newInstance(
            isCancel: Boolean = true,
            keyRequest: String? = null,

            anim: Int? = null,
            image: Int? = null,

            anchor: Background? = null,
            background: Background? = null,

            title: CharSequence? = null,
            message: CharSequence? = null,

            negative: ButtonInfo? = null,
            positive: ButtonInfo? = null
        ) = HorizontalConfirmDialogFragment().apply {

            arguments = bundleOf(
                Param.CANCEL to isCancel,

                Param.KEY_REQUEST to keyRequest,

                Param.ANIM to anim,
                Param.IMAGE to image,

                Param.ANCHOR to anchor,
                Param.BACKGROUND to background,

                Param.TITLE to title,
                Param.MESSAGE to message,

                Param.NEGATIVE to negative,
                Param.POSITIVE to positive
            )
        }
    }
}