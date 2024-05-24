package com.simple.coreapp.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.simple.core.utils.extentions.orZero
import com.simple.coreapp.Param
import com.simple.coreapp.databinding.DialogToastBinding
import com.simple.coreapp.entities.ToastType
import com.simple.coreapp.ui.base.dialogs.BaseViewBindingDialogFragment
import com.simple.coreapp.utils.ext.doOnHeightStatusAndHeightNavigationChange
import com.simple.coreapp.utils.ext.setDebouncedClickListener
import com.simple.coreapp.utils.ext.setVisible
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ToastDialog : BaseViewBindingDialogFragment<DialogToastBinding>() {

    override fun onStart() {
        super.onStart()
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog?.window?.setGravity(Gravity.TOP)

//        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
//        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
//        val dialogWidth = min(screenWidth, screenHeight) * 0.95f
//
//        dialog?.window?.setLayout(dialogWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
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

        binding.ivClose.setDebouncedClickListener {

            dismiss()
        }

        viewLifecycleOwner.lifecycleScope.launch {

//            delay(3 * 1000)
//            dismiss()
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
