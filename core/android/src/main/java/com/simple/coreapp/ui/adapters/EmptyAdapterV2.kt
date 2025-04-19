package com.simple.coreapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.simple.coreapp.databinding.ItemEmptyBinding
import com.tuanha.adapter.annotation.ItemAdapter

@ItemAdapter
class EmptyAdapterV2(onItemClick: ((View, EmptyViewItemV2) -> Unit)? = null) : com.tuanha.adapter.ViewItemAdapter<EmptyViewItemV2, ItemEmptyBinding>(onItemClick) {

    override val viewItemClass: Class<EmptyViewItemV2> by lazy {
        EmptyViewItemV2::class.java
    }

    override fun createViewBinding(parent: ViewGroup, viewType: Int): ItemEmptyBinding {
        return ItemEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(binding: ItemEmptyBinding, viewType: Int, position: Int, item: EmptyViewItemV2) {

        binding.root.transitionName = item.id

        binding.tvMessage.text = item.message
        binding.lottieAnimationView.setAnimation(item.imageRes)
    }
}

class EmptyViewItemV2(
    val id: String = "",

    var message: CharSequence = "",

    var imageRes: Int = 0,
) : com.tuanha.adapter.entities.ViewItem {

    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )
}