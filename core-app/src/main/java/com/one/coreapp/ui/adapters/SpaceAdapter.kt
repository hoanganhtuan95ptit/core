package com.one.coreapp.ui.adapters

import com.one.adapter.ViewItemAdapter
import com.one.adapter.ViewItemCloneable
import com.one.coreapp.databinding.ItemSpaceBinding
import com.one.coreapp.utils.extentions.resize
import java.util.*

class SpaceAdapter : ViewItemAdapter<SpaceViewItem, ItemSpaceBinding>() {

    override fun bind(binding: ItemSpaceBinding, viewType: Int, position: Int, item: SpaceViewItem) {
        super.bind(binding, viewType, position, item)

        binding.root.resize(width = item.size, height = item.size)
    }
}

data class SpaceViewItem(val size: Int) : ViewItemCloneable {

    override fun clone() = copy()

    override fun areItemsTheSame(): List<Any> = listOf(
        UUID.randomUUID().toString()
    )
}