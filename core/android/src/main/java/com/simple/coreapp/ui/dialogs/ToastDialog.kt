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

        val message = arguments?.getString(Param.PARAM_MESSAGE)
        binding.tvMessage.text = message

        val image = arguments?.getInt(Param.PARAM_IMAGE).orZero()
        binding.ivImage.setImageResource(image)
        binding.ivImage.setVisible(image != 0)

        val type = arguments?.getString(Param.STATE).orEmpty().toToastType()
        if (type == ToastType.SUCCESS) {
            binding.frameContent.setBackgroundResource(R.drawable.bg_corner_12dp_solid_primary_variant)
        } else if (type == ToastType.ERROR) {
            binding.frameContent.setBackgroundResource(R.drawable.bg_corner_12dp_solid_error_variant)
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
                Param.PARAM_IMAGE to image,
                Param.PARAM_MESSAGE to message,
            )
        }
    }
}
