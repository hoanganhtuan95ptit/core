package com.simple.adapter

import android.view.LayoutInflater
import com.simple.adapter.databinding.ItemLoadingBinding
import com.simple.adapter.entities.ViewItem

class LoadingViewAdapter : ViewItemAdapter<LoadingViewItem, ItemLoadingBinding>() {

    override fun bind(binding: ItemLoadingBinding, viewType: Int, position: Int, item: LoadingViewItem) {
        super.bind(binding, viewType, position, item)

        val parent = binding.root

        binding.root.addView(LayoutInflater.from(parent.context)?.inflate(item.data, parent, false))
    }
}

data class LoadingViewItem(
    val data: Int
) : ViewItem {

    override fun areItemsTheSame(): List<Any> = listOf(TAG, data)

    companion object {

        const val TAG = "LoadingViewItem"
    }
}