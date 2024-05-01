package com.simple.adapter

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.simple.adapter.databinding.ItemLoadMoreBinding
import com.simple.adapter.entities.ViewItem

class LoadMoreViewAdapter(
    private val onLoadMore: (() -> Unit)? = null,
) : ViewItemAdapter<LoadMoreViewItem, ItemLoadMoreBinding>() {

    override fun onViewAttachedToWindow(holder: BaseBindingViewHolder<ViewBinding>) {
        super.onViewAttachedToWindow(holder)

        onLoadMore?.invoke()
    }

    override fun bind(binding: ItemLoadMoreBinding, viewType: Int, position: Int, item: LoadMoreViewItem) {

        val layoutParams = binding.root.layoutParams as? StaggeredGridLayoutManager.LayoutParams ?: return
        layoutParams.isFullSpan = true
    }
}

data class LoadMoreViewItem(
    val id: String = ""
) : ViewItem {

    override fun areItemsTheSame(): List<Any> = listOf(
        javaClass.simpleName, id
    )
}