@file:Suppress("UNCHECKED_CAST")

package com.one.coreapp.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import com.one.coreapp.R
import com.one.coreapp.databinding.FragmentOptionsBinding
import com.one.coreapp.databinding.ItemOptionBinding
import com.one.coreapp.ui.base.adapters.BaseAsyncAdapter
import com.one.coreapp.ui.base.adapters.ViewItem
import com.one.coreapp.ui.base.dialogs.BaseViewBindingSheetFragment
import com.one.coreapp.utils.extentions.setDebouncedClickListener
import java.io.Serializable
import kotlin.math.min

class OptionFragment : BaseViewBindingSheetFragment<FragmentOptionsBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRec()
    }

    private fun setupRec() = OptionAdapter { _, item ->

        setFragmentResult(requireArguments().getString(KEY_REQUEST, ""), bundleOf(requireArguments().getString(KEY_DATA, "") to item))
        dismiss()
    }.apply {

        val list = requireArguments().getSerializable(DATA) as List<OptionViewItem>

        submitList(list)

        binding?.recyclerView?.layoutManager = GridLayoutManager(requireContext(), min(4, list.size))
        binding?.recyclerView?.adapter = this
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

class OptionAdapter(
    private val onItemClickListener: (View, OptionViewItem) -> Unit = { _, _ -> }
) : BaseAsyncAdapter<OptionViewItem, ItemOptionBinding>() {

    override fun createView(parent: ViewGroup, viewType: Int?): ItemOptionBinding {

        val binding = ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.root.setDebouncedClickListener { v ->

            getViewItem<OptionViewItem>(binding)?.let { onItemClickListener.invoke(v, it) }
        }

        return binding
    }

    override fun bind(binding: ItemOptionBinding, viewType: Int, position: Int, item: OptionViewItem) {
        super.bind(binding, viewType, position, item)

        binding.tvOption.setText(item.textRes)
        binding.ivOption.setImageResource(item.imageRes)
    }
}

data class OptionViewItem(
    val textRes: Int = 0,
    var imageRes: Int = 0,
) : ViewItem, Serializable {

    fun clone(): OptionViewItem = OptionViewItem(textRes, imageRes)

    fun refresh() {}

    override fun areItemsTheSame(): List<Any> = listOf(
        textRes, imageRes
    )
}