package com.simple.coreapp.ui.adapters

import android.view.View
import com.simple.adapter.ViewItemAdapter
import com.simple.adapter.entities.ViewItem
import com.simple.coreapp.databinding.ItemTextBinding
import com.simple.coreapp.ui.view.Margin
import com.simple.coreapp.ui.view.Padding
import com.simple.coreapp.ui.view.Size
import com.simple.coreapp.ui.view.TextStyle
import com.simple.coreapp.ui.view.round.Background
import com.simple.coreapp.ui.view.round.setBackground
import com.simple.coreapp.ui.view.setMargin
import com.simple.coreapp.ui.view.setPadding
import com.simple.coreapp.ui.view.setSize
import com.simple.coreapp.ui.view.setTextStyle
import com.simple.coreapp.utils.ext.setVisible
import com.simple.image.setImage

open class TextAdapter(onItemClick: (View, TextViewItem) -> Unit = { _, _ -> }) : ViewItemAdapter<TextViewItem, ItemTextBinding>(onItemClick) {

    override fun bind(binding: ItemTextBinding, viewType: Int, position: Int, item: TextViewItem, payloads: MutableList<Any>) {
        super.bind(binding, viewType, position, item, payloads)

        if (payloads.contains(TEXT)) {
            refreshText(binding, item)
        }
    }

    override fun bind(binding: ItemTextBinding, viewType: Int, position: Int, item: TextViewItem) {
        super.bind(binding, viewType, position, item)

        binding.root.transitionName = item.id

        binding.root.setSize(item.size)
        binding.root.setMargin(item.margin)
        binding.root.setPadding(item.padding)

        item.image?.end?.let {
            binding.ivEnd.setImage(it)
        }
        binding.ivEnd.setVisible(item.image?.end != null)

        item.image?.start?.let {
            binding.ivStart.setImage(it)
        }
        binding.ivStart.setVisible(item.image?.start != null)

        binding.tvTitle.setTextStyle(item.textStyle)
        binding.root.delegate.setBackground(item.background)

        refreshText(binding, item)
    }

    private fun refreshText(binding: ItemTextBinding, item: TextViewItem) {

        binding.tvTitle.text = item.text
    }
}

data class TextViewItem(
    val id: String = "",
    val data: Any? = null,

    var text: CharSequence = "",

    val size: Size? = null,
    val image: Image? = null,
    val margin: Margin? = null,
    val padding: Padding? = null,
    var textStyle: TextStyle? = null,
    var background: Background? = null
) : ViewItem {

    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )

    override fun getContentsCompare(): List<Pair<Any, String>> = listOf(
        text to TEXT
    )

    data class Image(
        val end: Int? = null,
        val start: Int? = null
    )
}

private const val TEXT = "TEXT"