package com.simple.coreapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.simple.coreapp.databinding.ItemEmptyBinding
import com.simple.adapter.annotation.ItemAdapter
import com.simple.adapter.entities.ViewItem
import com.simple.coreapp.utils.ext.RichText
import com.simple.coreapp.utils.ext.emptyText
import com.simple.coreapp.utils.ext.setText

@ItemAdapter
class EmptyAdapter(onItemClick: ((View, EmptyViewItem) -> Unit)? = null) : com.simple.adapter.ViewItemAdapter<EmptyViewItem, ItemEmptyBinding>(onItemClick) {

    override val viewItemClass: Class<EmptyViewItem> by lazy {
        EmptyViewItem::class.java
    }

    override fun createViewBinding(parent: ViewGroup, viewType: Int): ItemEmptyBinding {
        return ItemEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(binding: ItemEmptyBinding, viewType: Int, position: Int, item: EmptyViewItem) {

        binding.root.transitionName = item.id

        binding.tvMessage.setText(item.message)
        binding.lottieAnimationView.setAnimation(item.imageRes)
    }
}

data class EmptyViewItem(
    val id: String = "",

    var message: RichText = emptyText(),

    var imageRes: Int = 0,
) : ViewItem {

    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )
}