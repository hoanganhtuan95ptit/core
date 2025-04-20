package com.simple.coreapp.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.simple.coreapp.databinding.ItemSpaceBinding
import com.simple.adapter.ViewItemAdapter
import com.simple.adapter.annotation.ItemAdapter
import com.simple.adapter.entities.ViewItem

@ItemAdapter
class SpaceAdapter : ViewItemAdapter<SpaceViewItem, ItemSpaceBinding>() {

    override val viewItemClass: Class<SpaceViewItem> by lazy {
        SpaceViewItem::class.java
    }

    override fun createViewBinding(parent: ViewGroup, viewType: Int): ItemSpaceBinding {
        return ItemSpaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(binding: ItemSpaceBinding, viewType: Int, position: Int, item: SpaceViewItem) {
        super.onBindViewHolder(binding, viewType, position, item)

        binding.space.updateLayoutParams {

            width = item.width
            height = item.height
        }

        if (item.background == null) {

            binding.root.setBackgroundColor(Color.TRANSPARENT)
        } else {

            binding.root.setBackgroundResource(item.background)
        }
    }
}

data class SpaceViewItem(
    val id: String = "",

    val width: Int = ViewGroup.LayoutParams.MATCH_PARENT,
    val height: Int = ViewGroup.LayoutParams.MATCH_PARENT,

    val background: Int? = null
) : ViewItem {

    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )
}