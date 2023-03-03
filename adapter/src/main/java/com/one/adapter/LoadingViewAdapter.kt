package com.one.adapter

import android.view.LayoutInflater
import androidx.core.view.children
import com.one.adapter.databinding.ItemLoadingBinding
import io.supercharge.shimmerlayout.ShimmerLayout

class LoadingViewAdapter : ViewItemAdapter<LoadingViewItem, ItemLoadingBinding>() {

    override fun onViewAttachedToWindow(binding: ItemLoadingBinding) {
        super.onViewAttachedToWindow(binding)

        binding.root.let { it.children.filterIsInstance<ShimmerLayout>().firstOrNull() ?: it }.startShimmerAnimation()
    }

    override fun onViewDetachedFromWindow(binding: ItemLoadingBinding) {
        super.onViewDetachedFromWindow(binding)

        binding.root.let { it.children.filterIsInstance<ShimmerLayout>().firstOrNull() ?: it }.stopShimmerAnimation()
    }

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