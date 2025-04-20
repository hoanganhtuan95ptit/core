package com.simple.coreapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.simple.adapter.ViewItemAdapter
import com.simple.adapter.annotation.ItemAdapter
import com.simple.adapter.base.BaseBindingViewHolder
import com.simple.core.utils.extentions.asObjectOrNull
import com.simple.coreapp.databinding.ItemImageBinding
import com.simple.coreapp.ui.view.Background
import com.simple.coreapp.ui.view.Size
import com.simple.coreapp.ui.view.setBackground
import com.simple.coreapp.ui.view.setSize
import com.simple.event.sendEvent
import com.simple.image.setImage

@ItemAdapter
class ImageAdapter(val onItemClick: ((View, ImageViewItem) -> Unit)? = null) : ViewItemAdapter<ImageViewItem, ItemImageBinding>() {

    override val viewItemClass: Class<ImageViewItem> by lazy {
        ImageViewItem::class.java
    }

    override fun createViewBinding(parent: ViewGroup, viewType: Int): ItemImageBinding {

        return ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun createViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ItemImageBinding> {

        val viewHolder = BaseBindingViewHolder(createViewBinding(parent, viewType), viewType)

        val binding = viewHolder.binding

        binding.root.setOnClickListener { view ->

            val viewItem = getViewItem(viewHolder.bindingAdapterPosition) ?: return@setOnClickListener

            onItemClick?.invoke(view, viewItem)
            sendEvent("IMAGE_VIEW_ITEM_CLICKED", viewItem)
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

    override fun onBindViewHolder(binding: ItemImageBinding, viewType: Int, position: Int, item: ImageViewItem, payloads: MutableList<Any>) {

        binding.root.transitionName = item.id

        if (payloads.contains(PAYLOAD_SIZE)) refreshSize(binding, item)
        if (payloads.contains(PAYLOAD_IMAGE)) refreshImage(binding, item)
        if (payloads.contains(PAYLOAD_BACKGROUND)) refreshBackground(binding, item)
    }

    override fun onBindViewHolder(binding: ItemImageBinding, viewType: Int, position: Int, item: ImageViewItem) {

        binding.root.transitionName = item.id

        refreshSize(binding, item)
        refreshImage(binding, item)
        refreshBackground(binding, item)
    }

    private fun refreshSize(binding: ItemImageBinding, item: ImageViewItem) {

        binding.root.setSize(item.size)
    }

    private fun refreshImage(binding: ItemImageBinding, item: ImageViewItem) {

        if (item.anim != null) {

            binding.ivImage.setAnimation(item.anim!!)
        } else if (item.image != null) {

            binding.ivImage.setImage(item.image!!)
        }
    }

    private fun refreshBackground(binding: ItemImageBinding, item: ImageViewItem) {

        binding.root.delegate.setBackground(item.background)
    }
}

class ImageViewItem(
    val id: String = "",

    var anim: Int? = null,
    var image: String? = null,

    val size: Size? = null,
    var background: Background? = null,
) : com.simple.adapter.entities.ViewItem {

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