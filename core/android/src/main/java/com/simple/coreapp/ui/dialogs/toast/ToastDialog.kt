package com.simple.coreapp.ui.dialogs.toast

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
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
import com.simple.coreapp.utils.ext.RichText
import com.simple.coreapp.utils.ext.setDebouncedClickListener
import com.simple.coreapp.utils.ext.setText
import com.simple.coreapp.utils.ext.setVisible
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

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

        val viewModel by activityViewModels<ToastViewModel>()

        val id = arguments?.getString(Param.ID).orEmpty()

        viewModel.infoMap[ToastViewModel.Id(id)]?.let {

            val binding = binding ?: return

            val message = it.message
            binding.tvMessage.setText(message)

            val image = it.image
            binding.ivImage.setImageResource(image)
            binding.ivImage.setVisible(image != 0)

            val imageClose = it.imageClose
            binding.ivClose.setImageResource(imageClose)
            binding.ivClose.setVisible(imageClose != 0)

            val margin = it.margin
            binding.frameContent.setMargin(margin)

            val padding = it.padding
            binding.frameContent.setPadding(padding)

            val background = it.background
            binding.frameContent.delegate.setBackground(background)
        }

        val binding = binding ?: return

        binding.ivClose.setDebouncedClickListener {

            dismiss()
        }

        viewLifecycleOwner.lifecycleScope.launch {

            delay(5 * 1000)
            if (isAdded && !isStateSaved) dismiss()
        }
    }

    companion object {

        fun newInstance(
            activity: FragmentActivity,

            image: Int? = null,
            imageClose: Int? = null,

            message: RichText? = null,

            margin: Margin? = null,
            padding: Padding? = null,
            background: Background? = null
        ) = ToastDialog().apply {

            val id = UUID.randomUUID().toString()

            activity.viewModels<ToastViewModel>().value.updateInfo(
                id = id,

                image = image.orZero(),
                imageClose = imageClose.orZero(),

                message = message,

                margin = margin,
                padding = padding,
                background = background
            )

            arguments = bundleOf(
                Param.ID to id,
            )
        }
    }
}
