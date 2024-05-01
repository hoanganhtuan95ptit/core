package com.simple.coreapp.ui.adapters

import android.view.ViewGroup
import com.simple.adapter.ViewItemAdapter
import com.simple.adapter.entities.ViewItem
import com.simple.coreapp.databinding.ItemSpaceBinding
import java.util.UUID

class SpaceAdapter : ViewItemAdapter<SpaceViewItem, ItemSpaceBinding>() {

    override fun bind(binding: ItemSpaceBinding, viewType: Int, position: Int, item: SpaceViewItem) {
        super.bind(binding, viewType, position, item)

        binding.root.resize(width = item.width, height = item.height)

        if (item.background > 0) binding.root.setBackgroundResource(item.background)
    }
}

data class SpaceViewItem(
    val id: String = UUID.randomUUID().toString(),

    val width: Int = ViewGroup.LayoutParams.MATCH_PARENT,
    val height: Int = ViewGroup.LayoutParams.MATCH_PARENT,

    val background: Int = 0
) : ViewItem {

    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )
}