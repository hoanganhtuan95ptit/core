package com.simple.coreapp.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.simple.core.utils.extentions.orZero
import com.simple.coreapp.Param
import com.simple.coreapp.R
import com.simple.coreapp.databinding.DialogToastBinding
import com.simple.coreapp.entities.ToastType
import com.simple.coreapp.entities.ToastType.Companion.toToastType
import com.simple.coreapp.ui.base.dialogs.BaseViewBindingDialogFragment
import com.simple.coreapp.utils.ext.setDebouncedClickListener
import com.simple.coreapp.utils.ext.setVisible
import com.simple.coreapp.utils.extentions.getColorFromAttr
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ToastDialog : BaseViewBindingDialogFragment<DialogToastBinding>() {

    override fun onStart() {
        super.onStart()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog?.window?.setGravity(Gravity.TOP)

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = binding ?: return

        val message = arguments?.getString(Param.MESSAGE)
        binding.tvMessage.text = message

        val image = arguments?.getInt(Param.IMAGE).orZero()
        binding.ivImage.setImageResource(image)
        binding.ivImage.setVisible(image != 0)

        val type = arguments?.getString(Param.STATE).orEmpty().toToastType()
        if (type == ToastType.SUCCESS) {
            binding.tvMessage.setTextColor(view.context.getColorFromAttr(R.attr.colorOnToastSuccess))
            binding.frameContent.setBackgroundResource(R.drawable.bg_corner_12dp_solid_toast_success)
        } else if (type == ToastType.ERROR) {
            binding.tvMessage.setTextColor(view.context.getColorFromAttr(R.attr.colorOnToastError))
            binding.frameContent.setBackgroundResource(R.drawable.bg_corner_12dp_solid_toast_error)
        }

        binding.ivClose.setDebouncedClickListener {

            dismiss()
        }

        viewLifecycleOwner.lifecycleScope.launch {

            delay(5 * 1000)
            dismiss()
        }
    }

    companion object {

        fun newInstance(
            type: ToastType,
            image: Int? = null,
            message: String? = null,
        ) = ToastDialog().apply {

            arguments = bundleOf(
                Param.STATE to type.name,

                Param.IMAGE to image,
                Param.MESSAGE to message,
            )
        }
    }
}
