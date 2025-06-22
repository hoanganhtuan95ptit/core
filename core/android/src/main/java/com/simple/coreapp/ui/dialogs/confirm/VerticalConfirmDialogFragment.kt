package com.simple.coreapp.ui.dialogs.confirm

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.simple.core.utils.extentions.orZero
import com.simple.coreapp.Param
import com.simple.coreapp.databinding.DialogConfirmVerticalBinding
import com.simple.coreapp.ui.base.dialogs.sheet.BaseViewBindingSheetFragment
import com.simple.coreapp.ui.view.Background
import com.simple.coreapp.ui.view.setBackground
import com.simple.coreapp.utils.ext.ButtonInfo
import com.simple.coreapp.utils.ext.RichText
import com.simple.coreapp.utils.ext.doOnHeightStatusAndHeightNavigationChange
import com.simple.coreapp.utils.ext.setDebouncedClickListener
import com.simple.coreapp.utils.ext.setText
import com.simple.coreapp.utils.ext.setVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

class VerticalConfirmDialogFragment : BaseViewBindingSheetFragment<DialogConfirmVerticalBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doOnHeightStatusAndHeightNavigationChange { _, heightNavigationBar ->

            binding?.root?.setPadding(0, 0, 0, heightNavigationBar)
        }

        val isCancelable = arguments?.getBoolean(Param.CANCEL) == true
        dialog?.setCancelable(isCancelable)
        dialog?.setCanceledOnTouchOutside(isCancelable)


        val viewModel by activityViewModels<ConfirmViewModel>()

        val id = arguments?.getString(Param.ID).orEmpty()
        val keyRequest = arguments?.getString(Param.KEY_REQUEST).orEmpty()

        viewModel.infoMap[ConfirmViewModel.Id(id, keyRequest)]?.let {

            val binding = binding ?: return


            val anim = it.anim
            if (anim != 0) binding.ivLogo.setAnimation(anim)

            val image = it.image
            if (image != 0) binding.ivLogo.setImageResource(image)

            binding.ivLogo.setVisible(anim != 0 || image != 0)


            val title = it.title
            binding.tvTitle.setText(title)
            binding.tvTitle.setVisible(!title?.text.isNullOrBlank())

            val message = it.message
            binding.tvMessage.setText(message)
            binding.tvMessage.setVisible(!message?.text.isNullOrBlank())

            val anchor = it.anchor
            binding.anchor.delegate.setBackground(anchor)

            val background = it.background
            binding.root.delegate.setBackground(background)

            val negative = it.negative
            binding.tvNegative.setVisible(negative != null)
            binding.tvNegative.setText(negative?.text)
            binding.tvNegative.delegate.setBackground(negative?.background)

            val positive = it.positive
            binding.tvPositive.setVisible(positive != null)
            binding.tvPositive.setText(positive?.text)
            binding.tvPositive.delegate.setBackground(positive?.background)
        }


        val binding = binding ?: return

        binding.tvNegative.setDebouncedClickListener {

            setFragmentResult(arguments?.getString(Param.KEY_REQUEST).orEmpty(), bundleOf(Param.RESULT_CODE to 0))
            dismiss()
        }

        binding.tvPositive.setDebouncedClickListener {

            setFragmentResult(arguments?.getString(Param.KEY_REQUEST).orEmpty(), bundleOf(Param.RESULT_CODE to 1))
            dismiss()
        }
    }

    companion object {

        fun newInstance(
            activity: FragmentActivity,

            isCancel: Boolean = true,
            keyRequest: String? = null,

            anim: Int? = null,
            image: Int? = null,

            anchor: Background? = null,
            background: Background? = null,

            title: RichText? = null,
            message: RichText? = null,

            negative: ButtonInfo? = null,
            positive: ButtonInfo? = null
        ) = VerticalConfirmDialogFragment().apply {

            val id = UUID.randomUUID().toString()
            val keyRequestWrap = keyRequest.orEmpty()


            activity.viewModels<ConfirmViewModel>().value.updateInfo(
                id = id,
                keyRequest = keyRequestWrap,

                anim = anim.orZero(),
                image = image.orZero(),

                anchor = anchor,
                background = background,

                title = title,
                message = message,

                negative = negative,
                positive = positive
            )

            arguments = bundleOf(

                Param.CANCEL to isCancel,

                Param.ID to id,
                Param.KEY_REQUEST to keyRequestWrap,
            )
        }
    }
}