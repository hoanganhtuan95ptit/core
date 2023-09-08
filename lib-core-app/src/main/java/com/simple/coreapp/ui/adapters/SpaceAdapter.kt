package com.simple.coreapp.ui.adapters

import android.view.ViewGroup
import com.simple.adapter.ViewItemAdapter
import com.simple.adapter.ViewItemCloneable
import com.simple.coreapp.databinding.ItemSpaceBinding
import com.simple.coreapp.utils.extentions.resize
import java.util.*

class SpaceAdapter : ViewItemAdapter<SpaceViewItem, ItemSpaceBinding>() {

    override fun bind(binding: ItemSpaceBinding, viewType: Int, position: Int, item: SpaceViewItem) {
        super.bind(binding, viewType, position, item)

        binding.root.resize(width = item.width, height = item.height)
    }
}

data class SpaceViewItem(val width: Int = ViewGroup.LayoutParams.MATCH_PARENT, val height: Int = ViewGroup.LayoutParams.MATCH_PARENT) : ViewItemCloneable {

    override fun clone() = copy()

    override fun areItemsTheSame(): List<Any> = listOf(
        UUID.randomUUID().toString()
    )
}