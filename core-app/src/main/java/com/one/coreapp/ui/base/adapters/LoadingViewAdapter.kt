package com.one.coreapp.ui.base.adapters

import android.view.LayoutInflater
import com.one.coreapp.databinding.ItemLoadingBinding

class LoadingViewAdapter : ViewItemAdapter<LoadingViewItem, ItemLoadingBinding>() {

    override fun bind(binding: ItemLoadingBinding, viewType: Int, position: Int, item: LoadingViewItem) {
        super.bind(binding, viewType, position, item)

        val parent = binding.root

        binding.root.addView(LayoutInflater.from(parent.context)?.inflate(item.data, parent, false))
    }
}

data class LoadingViewItem(val data: Int) : ViewItemCloneable {

    override fun clone() = copy()

    override fun areItemsTheSame(): List<Any> = listOf("LoadingViewItem", data)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LoadingViewItem) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data
    }
}