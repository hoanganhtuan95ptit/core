package com.simple.coreapp.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.simple.adapter.ViewItemAdapter
import com.simple.adapter.annotation.ItemAdapter
import com.simple.adapter.entities.ViewItem
import com.simple.coreapp.databinding.ItemSpaceBinding
import com.simple.coreapp.ui.view.Size
import com.simple.coreapp.ui.view.setSize

@ItemAdapter
class SpaceAdapter : ViewItemAdapter<SpaceViewItem, ItemSpaceBinding>() {

    override val viewItemClass: Class<SpaceViewItem> by lazy {
        SpaceViewItem::class.java
    }

    override fun createViewBinding(parent: ViewGroup, viewType: Int): ItemSpaceBinding {
        return ItemSpaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(binding: ItemSpaceBinding, viewType: Int, position: Int, item: SpaceViewItem, payloads: MutableList<Any>) {
        super.onBindViewHolder(binding, viewType, position, item, payloads)

        if (payloads.contains(PAYLOAD_SIZE)) refreshSize(binding, item)
    }

    override fun onBindViewHolder(binding: ItemSpaceBinding, viewType: Int, position: Int, item: SpaceViewItem) {
        super.onBindViewHolder(binding, viewType, position, item)

        refreshSize(binding, item)

        if (item.background == null) {

            binding.root.setBackgroundColor(Color.TRANSPARENT)
        } else {

            binding.root.setBackgroundResource(item.background)
        }
    }

    private fun refreshSize(binding: ItemSpaceBinding, item: SpaceViewItem) {

        binding.space.setSize(item.size)
    }
}

fun SpaceViewItem(
    id: String = "",

    width: Int = ViewGroup.LayoutParams.MATCH_PARENT,
    height: Int = ViewGroup.LayoutParams.MATCH_PARENT,

    background: Int? = null
) = SpaceViewItem(
    id = id,
    size = Size(width = width, height = height),
    background = background
)

data class SpaceViewItem(
    val id: String = "",

    val size: Size = Size(
        width = ViewGroup.LayoutParams.MATCH_PARENT,
        height = ViewGroup.LayoutParams.MATCH_PARENT,
    ),

    val background: Int? = null
) : ViewItem {

    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )

    override fun getContentsCompare(): List<Pair<Any, String>> = listOf(
        size to PAYLOAD_SIZE,
    )
}

private const val PAYLOAD_SIZE = "PAYLOAD_SIZE"