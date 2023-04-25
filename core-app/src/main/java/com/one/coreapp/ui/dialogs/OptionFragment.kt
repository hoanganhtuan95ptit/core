@file:Suppress("UNCHECKED_CAST")

package com.one.coreapp.ui.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import com.one.adapter.MultiAdapter
import com.one.adapter.ViewItemAdapter
import com.one.adapter.ViewItemCloneable
import com.one.coreapp.databinding.FragmentOptionsBinding
import com.one.coreapp.databinding.ItemOptionBinding
import com.one.coreapp.ui.base.fragments.BaseViewBindingSheetFragment
import kotlin.math.min

class OptionFragment : BaseViewBindingSheetFragment<FragmentOptionsBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRec()
    }

    private fun setupRec() {

        val binding = binding ?: return

        val list = requireArguments().getSerializable(DATA) as List<OptionViewItem>

        val optionAdapter = OptionAdapter { _, item ->

            setFragmentResult(requireArguments().getString(KEY_REQUEST, ""), bundleOf(requireArguments().getString(KEY_DATA, "") to item))
            dismiss()
        }

        MultiAdapter(optionAdapter).apply {

            binding.recyclerView.adapter = this
            binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), min(4, list.size))

            submitList(list)
        }
    }

    companion object {

        const val DATA = "DATA"
        const val KEY_DATA = "KEY_DATA"
        const val KEY_REQUEST = "KEY_REQUEST"

        fun newInstance(keyData: String, keyRequest: String, list: List<OptionViewItem>) = OptionFragment().apply {

            arguments = bundleOf(
                DATA to list,
                KEY_DATA to keyData,
                KEY_REQUEST to keyRequest,
            )
        }
    }
}

class OptionAdapter(onItemClick: (View, OptionViewItem) -> Unit = { _, _ -> }) : ViewItemAdapter<OptionViewItem, ItemOptionBinding>(onItemClick) {

    override fun bind(binding: ItemOptionBinding, viewType: Int, position: Int, item: OptionViewItem) {
        super.bind(binding, viewType, position, item)

        binding.tvOption.setText(item.textRes)
        binding.ivOption.setImageResource(item.imageRes)
    }
}

data class OptionViewItem(
    val textRes: Int = 0,
    var imageRes: Int = 0,
) : ViewItemCloneable {

    override fun clone() = copy()

    override fun areItemsTheSame(): List<Any> = listOf(
        textRes, imageRes
    )
}