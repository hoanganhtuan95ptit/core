package com.simple.coreapp.ui.adapters

import android.view.View
import com.simple.adapter.ViewItemAdapter
import com.simple.adapter.entities.ViewItem
import com.simple.coreapp.databinding.ItemEmptyBinding

open class EmptyAdapter(onItemClick: (View, EmptyViewItem) -> Unit = { _, _ -> }) : ViewItemAdapter<EmptyViewItem, ItemEmptyBinding>(onItemClick) {

    override fun bind(binding: ItemEmptyBinding, viewType: Int, position: Int, item: EmptyViewItem) {
        super.bind(binding, viewType, position, item)

        binding.tvMessage.text = item.message

        binding.lottieAnimationView.setAnimation(item.imageRes)
    }
}

class EmptyViewItem(
    val id: String = "",

    var message: CharSequence = "",

    var imageRes: Int = 0,
) : ViewItem {

    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )
}