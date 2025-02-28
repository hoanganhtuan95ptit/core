package com.simple.coreapp.ui.adapters

import android.view.View
import com.simple.adapter.ViewItemAdapter
import com.simple.adapter.entities.ViewItem
import com.simple.coreapp.databinding.ItemImageBinding
import com.simple.coreapp.ui.view.Background
import com.simple.coreapp.ui.view.Size
import com.simple.coreapp.ui.view.setBackground
import com.simple.coreapp.ui.view.setSize
import com.simple.image.setImage

open class ImageAdapter(
    onItemClick: (View, ImageViewItem) -> Unit
) : ViewItemAdapter<ImageViewItem, ItemImageBinding>(onItemClick) {

    override fun bind(binding: ItemImageBinding, viewType: Int, position: Int, item: ImageViewItem, payloads: MutableList<Any>) {
        super.bind(binding, viewType, position, item, payloads)

        if (payloads.contains(PAYLOAD_SIZE)) refreshSize(binding, item)
        if (payloads.contains(PAYLOAD_IMAGE)) refreshImage(binding, item)
        if (payloads.contains(PAYLOAD_BACKGROUND)) refreshBackground(binding, item)
    }

    override fun bind(binding: ItemImageBinding, viewType: Int, position: Int, item: ImageViewItem) {
        super.bind(binding, viewType, position, item)

        refreshSize(binding, item)
        refreshImage(binding, item)
        refreshBackground(binding, item)
    }

    private fun refreshSize(binding: ItemImageBinding, item: ImageViewItem) {

        binding.root.setSize(item.size)
    }

    private fun refreshImage(binding: ItemImageBinding, item: ImageViewItem) {

        binding.ivImage.setImage(item.image)
    }

    private fun refreshBackground(binding: ItemImageBinding, item: ImageViewItem) {

        binding.root.delegate.setBackground(item.background)
    }
}

class ImageViewItem(
    val id: String = "",

    var image: String = "",

    val size: Size? = null,
    var background: Background? = null,
) : ViewItem {

    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )

    override fun getContentsCompare(): List<Pair<Any, String>> = listOf(
        image to PAYLOAD_IMAGE,
        (size ?: PAYLOAD_SIZE) to PAYLOAD_SIZE,
        (background ?: PAYLOAD_BACKGROUND) to PAYLOAD_BACKGROUND
    )
}

private const val PAYLOAD_IMAGE = "PAYLOAD_IMAGE"
private const val PAYLOAD_SIZE = "PAYLOAD_SIZE"
private const val PAYLOAD_BACKGROUND = "PAYLOAD_BACKGROUND"