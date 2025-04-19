package com.simple.coreapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.simple.core.utils.extentions.asObjectOrNull
import com.simple.coreapp.databinding.ItemImageBinding
import com.simple.coreapp.ui.view.Background
import com.simple.coreapp.ui.view.Size
import com.simple.coreapp.ui.view.setBackground
import com.simple.coreapp.ui.view.setSize
import com.simple.coreapp.utils.ext.setDebouncedClickListener
import com.simple.image.setImage
import com.tuanha.adapter.annotation.ItemAdapter
import com.tuanha.adapter.base.BaseBindingViewHolder
import com.tuanha.event.sendEvent

@ItemAdapter
class ImageAdapterV2() : com.tuanha.adapter.ViewItemAdapter<ImageViewItemV2, ItemImageBinding>() {

    override val viewItemClass: Class<ImageViewItemV2> by lazy {
        ImageViewItemV2::class.java
    }

    override fun createViewBinding(parent: ViewGroup, viewType: Int): ItemImageBinding {

        return ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun createViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ItemImageBinding>? {

        val viewHolder = super.createViewHolder(parent, viewType) ?: return null

        viewHolder.binding.root.setDebouncedClickListener {

            val item = getViewItem(viewHolder.absoluteAdapterPosition) ?: return@setDebouncedClickListener
            sendEvent("CLICK_IMAGE", item)
        }

        return viewHolder
    }

    override fun onViewAttachedToWindow(holder: BaseBindingViewHolder<ViewBinding>) {
        super.onViewAttachedToWindow(holder)
        holder.binding.asObjectOrNull<ItemImageBinding>()?.ivImage?.playAnimation()
    }

    override fun onViewDetachedFromWindow(holder: BaseBindingViewHolder<ViewBinding>) {
        super.onViewDetachedFromWindow(holder)
        holder.binding.asObjectOrNull<ItemImageBinding>()?.ivImage?.pauseAnimation()
    }

    override fun onBindViewHolder(binding: ItemImageBinding, viewType: Int, position: Int, item: ImageViewItemV2, payloads: MutableList<Any>) {

        binding.root.transitionName = item.id

        if (payloads.contains(PAYLOAD_SIZE)) refreshSize(binding, item)
        if (payloads.contains(PAYLOAD_IMAGE)) refreshImage(binding, item)
        if (payloads.contains(PAYLOAD_BACKGROUND)) refreshBackground(binding, item)
    }

    override fun onBindViewHolder(binding: ItemImageBinding, viewType: Int, position: Int, item: ImageViewItemV2) {

        binding.root.transitionName = item.id

        refreshSize(binding, item)
        refreshImage(binding, item)
        refreshBackground(binding, item)
    }

    private fun refreshSize(binding: ItemImageBinding, item: ImageViewItemV2) {

        binding.root.setSize(item.size)
    }

    private fun refreshImage(binding: ItemImageBinding, item: ImageViewItemV2) {

        if (item.anim != null) {

            binding.ivImage.setAnimation(item.anim!!)
        } else if (item.image != null) {

            binding.ivImage.setImage(item.image!!)
        }
    }

    private fun refreshBackground(binding: ItemImageBinding, item: ImageViewItemV2) {

        binding.root.delegate.setBackground(item.background)
    }
}

class ImageViewItemV2(
    val id: String = "",

    var anim: Int? = null,
    var image: String? = null,

    val size: Size? = null,
    var background: Background? = null,
) : com.tuanha.adapter.entities.ViewItem {

    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )

    override fun getContentsCompare(): List<Pair<Any, String>> = listOf(
        (anim ?: image ?: PAYLOAD_IMAGE) to PAYLOAD_IMAGE,

        (size ?: PAYLOAD_SIZE) to PAYLOAD_SIZE,
        (background ?: PAYLOAD_BACKGROUND) to PAYLOAD_BACKGROUND
    )
}

private const val PAYLOAD_IMAGE = "PAYLOAD_IMAGE"

private const val PAYLOAD_SIZE = "PAYLOAD_SIZE"
private const val PAYLOAD_BACKGROUND = "PAYLOAD_BACKGROUND"