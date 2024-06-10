package com.simple.adapter

import android.graphics.Color
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.simple.adapter.databinding.ItemSpaceBinding
import com.simple.adapter.entities.ViewItem

class SpaceAdapter : ViewItemAdapter<SpaceViewItem, ItemSpaceBinding>() {

    override fun bind(binding: ItemSpaceBinding, viewType: Int, position: Int, item: SpaceViewItem) {
        super.bind(binding, viewType, position, item)

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