package com.one.adapter

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.one.adapter.databinding.ItemLoadMoreBinding
import java.util.*

class LoadMoreViewAdapter(
    private val onLoadMore: (() -> Unit)? = null,
) : ViewItemAdapter<LoadMoreViewItem, ItemLoadMoreBinding>() {

    override fun onViewAttachedToWindow(binding: ItemLoadMoreBinding) {
        super.onViewAttachedToWindow(binding)

        onLoadMore?.invoke()
    }

    override fun bind(binding: ItemLoadMoreBinding, viewType: Int, position: Int, item: LoadMoreViewItem) {

        val layoutParams = binding.root.layoutParams as? StaggeredGridLayoutManager.LayoutParams ?: return
        layoutParams.isFullSpan = true
    }
}

data class LoadMoreViewItem(val id: String = UUID.randomUUID().toString()) : ViewItemCloneable {

    override fun clone() = copy()

    override fun areItemsTheSame(): List<Any> = listOf(
        javaClass.simpleName, id
    )
}