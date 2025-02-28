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
import com.simple.coreapp.databinding.DialogToastBinding
import com.simple.coreapp.ui.base.dialogs.BaseViewBindingDialogFragment
import com.simple.coreapp.ui.view.Background
import com.simple.coreapp.ui.view.Margin
import com.simple.coreapp.ui.view.Padding
import com.simple.coreapp.ui.view.setBackground
import com.simple.coreapp.ui.view.setMargin
import com.simple.coreapp.ui.view.setPadding
import com.simple.coreapp.utils.ext.getParcelableOrNull
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

        val message = arguments?.getCharSequence(Param.MESSAGE)
        binding.tvMessage.text = message

        val image = arguments?.getInt(Param.IMAGE).orZero()
        binding.ivImage.setImageResource(image)
        binding.ivImage.setVisible(image != 0)

        val imageClose = arguments?.getInt(Param.IMAGE_CLOSE).orZero()
        binding.ivClose.setImageResource(imageClose)
        binding.ivClose.setVisible(imageClose != 0)

        val margin = arguments?.getParcelableOrNull<Margin>(Param.MARGIN)
        binding.frameContent.setMargin(margin)

        val padding = arguments?.getParcelableOrNull<Padding>(Param.PADDING)
        binding.frameContent.setPadding(padding)

        val background = arguments?.getParcelableOrNull<Background>(Param.BACKGROUND)
        binding.frameContent.delegate.setBackground(background)

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
            image: Int? = null,
            imageClose: Int? = null,

            message: CharSequence? = null,

            margin: Margin? = null,
            padding: Padding? = null,
            background: Background? = null
        ) = ToastDialog().apply {

            arguments = bundleOf(
                Param.IMAGE to image,
                Param.IMAGE_CLOSE to imageClose,

                Param.MESSAGE to message,

                Param.MARGIN to margin,
                Param.PADDING to padding,
                Param.BACKGROUND to background,
            )
        }
    }
}
