package com.simple.coreapp.ui.dialogs.options.adapters

import android.view.View
import android.view.ViewGroup
import com.simple.adapter.BaseBindingViewHolder
import com.simple.adapter.ViewItemAdapter
import com.simple.adapter.entities.ViewItem
import com.simple.coreapp.databinding.ItemOptionChooseBinding
import com.simple.image.setImage
import java.io.Serializable

class OptionAdapter(onItemClick: (View, OptionViewItem) -> Unit = { _, _ -> }) : ViewItemAdapter<OptionViewItem, ItemOptionChooseBinding>(onItemClick) {

    override fun createViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ItemOptionChooseBinding>? {

        val viewHolder = super.createViewHolder(parent, viewType)

        return viewHolder
    }

    override fun createViewItem(parent: ViewGroup, viewType: Int): ItemOptionChooseBinding {

        val binding = super.createViewItem(parent, viewType)

        return binding
    }

    override fun bind(binding: ItemOptionChooseBinding, viewType: Int, position: Int, item: OptionViewItem) {
        super.bind(binding, viewType, position, item)

        binding.tvTitle.text = item.text
        binding.ivTitle.setImage(item.image)
    }
}

data class OptionViewItem(
    val id: String = "",

    val text: String,
    val image: Int,

    val isEnable: Boolean = false
) : ViewItem, Serializable {

    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )

    override fun getContentsCompare(): List<Pair<Any, String>> = listOf(
        text to Payload.TEXT,
        image to Payload.IMAGE
    )
}

private object Payload {

    const val TEXT = "TEXT"
    const val IMAGE = "IMAGE"
}