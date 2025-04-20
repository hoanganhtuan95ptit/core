package com.simple.coreapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.simple.coreapp.databinding.ItemEmptyBinding
import com.tuanha.adapter.annotation.ItemAdapter

@ItemAdapter
class EmptyAdapter(onItemClick: ((View, EmptyViewItem) -> Unit)? = null) : com.tuanha.adapter.ViewItemAdapter<EmptyViewItem, ItemEmptyBinding>(onItemClick) {

    override val viewItemClass: Class<EmptyViewItem> by lazy {
        EmptyViewItem::class.java
    }

    override fun createViewBinding(parent: ViewGroup, viewType: Int): ItemEmptyBinding {
        return ItemEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(binding: ItemEmptyBinding, viewType: Int, position: Int, item: EmptyViewItem) {

        binding.root.transitionName = item.id

        binding.tvMessage.text = item.message
        binding.lottieAnimationView.setAnimation(item.imageRes)
    }
}

class EmptyViewItem(
    val id: String = "",

    var message: CharSequence = "",

    var imageRes: Int = 0,
) : com.tuanha.adapter.entities.ViewItem {

    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )
}