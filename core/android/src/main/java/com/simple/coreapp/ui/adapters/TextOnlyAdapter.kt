package com.simple.coreapp.ui.adapters

import android.view.View
import com.simple.adapter.ViewItemAdapter
import com.simple.adapter.entities.ViewItem
import com.simple.coreapp.databinding.ItemTextOnlyBinding
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

open class TextOnlyAdapter(onItemClick: (View, TextOnlyViewItem) -> Unit = { _, _ -> }) : ViewItemAdapter<TextOnlyViewItem, ItemTextOnlyBinding>(onItemClick) {

    override fun bind(binding: ItemTextOnlyBinding, viewType: Int, position: Int, item: TextOnlyViewItem, payloads: MutableList<Any>) {
        super.bind(binding, viewType, position, item, payloads)

        if (payloads.contains(PAYLOAD_SIZE)) refreshSize(binding, item)
        if (payloads.contains(PAYLOAD_TEXT)) refreshText(binding, item)
        if (payloads.contains(PAYLOAD_MARGIN)) refreshMargin(binding, item)
        if (payloads.contains(PAYLOAD_PADDING)) refreshPadding(binding, item)
        if (payloads.contains(PAYLOAD_TEXT_STYLE)) refreshTextStyle(binding, item)
        if (payloads.contains(PAYLOAD_BACKGROUND)) refreshBackground(binding, item)
    }

    override fun bind(binding: ItemTextOnlyBinding, viewType: Int, position: Int, item: TextOnlyViewItem) {
        super.bind(binding, viewType, position, item)

        binding.root.transitionName = item.id

        refreshSize(binding, item)
        refreshText(binding, item)
        refreshMargin(binding, item)
        refreshPadding(binding, item)
        refreshTextStyle(binding, item)
        refreshBackground(binding, item)
    }

    private fun refreshText(binding: ItemTextOnlyBinding, item: TextOnlyViewItem) {

        binding.tvTitle.text = item.text
    }

    private fun refreshSize(binding: ItemTextOnlyBinding, item: TextOnlyViewItem) {

        binding.root.setSize(item.size)
    }

    private fun refreshMargin(binding: ItemTextOnlyBinding, item: TextOnlyViewItem) {

        binding.root.setMargin(item.margin)
    }

    private fun refreshPadding(binding: ItemTextOnlyBinding, item: TextOnlyViewItem) {

        binding.root.setPadding(item.padding)
    }

    private fun refreshTextStyle(binding: ItemTextOnlyBinding, item: TextOnlyViewItem) {

        binding.tvTitle.setTextStyle(item.textStyle)
    }

    private fun refreshBackground(binding: ItemTextOnlyBinding, item: TextOnlyViewItem) {

        binding.root.delegate.setBackground(item.background)
    }
}

data class TextOnlyViewItem(
    val id: String = "",
    val data: Any? = null,

    var text: CharSequence = "",

    val size: Size? = null,
    val margin: Margin? = null,
    val padding: Padding? = null,
    var textStyle: TextStyle? = null,
    var background: Background? = null
) : ViewItem {

    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )

    override fun getContentsCompare(): List<Pair<Any, String>> = listOf(
        text to PAYLOAD_TEXT,
        (size ?: PAYLOAD_SIZE) to PAYLOAD_SIZE,
        (margin ?: PAYLOAD_MARGIN) to PAYLOAD_MARGIN,
        (padding ?: PAYLOAD_PADDING) to PAYLOAD_PADDING,
        (textStyle ?: PAYLOAD_TEXT_STYLE) to PAYLOAD_TEXT_STYLE,
        (background ?: PAYLOAD_BACKGROUND) to PAYLOAD_BACKGROUND
    )
}

private const val PAYLOAD_TEXT = "PAYLOAD_TEXT"
private const val PAYLOAD_SIZE = "PAYLOAD_SIZE"
private const val PAYLOAD_MARGIN = "PAYLOAD_MARGIN"
private const val PAYLOAD_PADDING = "PAYLOAD_PADDING"
private const val PAYLOAD_TEXT_STYLE = "PAYLOAD_TEXT_STYLE"
private const val PAYLOAD_BACKGROUND = "PAYLOAD_BACKGROUND"