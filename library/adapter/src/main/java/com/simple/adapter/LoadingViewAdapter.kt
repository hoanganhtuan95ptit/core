package com.simple.adapter

import android.view.LayoutInflater
import androidx.core.view.children
import androidx.viewbinding.ViewBinding
import com.simple.adapter.databinding.ItemLoadingBinding
import com.simple.adapter.entities.ViewItem
import com.simple.core.utils.extentions.asObject
import io.supercharge.shimmerlayout.ShimmerLayout

class LoadingViewAdapter : ViewItemAdapter<LoadingViewItem, ItemLoadingBinding>() {

    override fun onViewAttachedToWindow(holder: BaseBindingViewHolder<ViewBinding>) {
        super.onViewAttachedToWindow(holder)

        holder.binding.asObject<ItemLoadingBinding>().root.let { it.children.filterIsInstance<ShimmerLayout>().firstOrNull() ?: it }.startShimmerAnimation()
    }

    override fun onViewDetachedFromWindow(holder: BaseBindingViewHolder<ViewBinding>) {
        super.onViewDetachedFromWindow(holder)

        holder.binding.asObject<ItemLoadingBinding>().root.let { it.children.filterIsInstance<ShimmerLayout>().firstOrNull() ?: it }.stopShimmerAnimation()
    }

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