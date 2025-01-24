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

        if (payloads.contains(PAYLOAD_SIZE)) refreshSize(binding, item)
        if (payloads.contains(PAYLOAD_TEXT)) refreshText(binding, item)
        if (payloads.contains(PAYLOAD_IMAGE)) refreshImage(binding, item)
        if (payloads.contains(PAYLOAD_MARGIN)) refreshMargin(binding, item)
        if (payloads.contains(PAYLOAD_PADDING)) refreshPadding(binding, item)
        if (payloads.contains(PAYLOAD_TEXT_STYLE)) refreshTextStyle(binding, item)
        if (payloads.contains(PAYLOAD_BACKGROUND)) refreshBackground(binding, item)
    }

    override fun bind(binding: ItemTextBinding, viewType: Int, position: Int, item: TextViewItem) {
        super.bind(binding, viewType, position, item)

        binding.root.transitionName = item.id

        refreshSize(binding, item)
        refreshText(binding, item)
        refreshImage(binding, item)
        refreshMargin(binding, item)
        refreshPadding(binding, item)
        refreshTextStyle(binding, item)
        refreshBackground(binding, item)
    }

    private fun refreshText(binding: ItemTextBinding, item: TextViewItem) {

        binding.tvTitle.text = item.text
    }

    private fun refreshSize(binding: ItemTextBinding, item: TextViewItem) {

        binding.root.setSize(item.size)
    }

    private fun refreshImage(binding: ItemTextBinding, item: TextViewItem) {

        item.image?.end?.let {
            binding.ivEnd.setImage(it)
        }
        binding.ivEnd.setVisible(item.image?.end != null)

        item.image?.start?.let {
            binding.ivStart.setImage(it)
        }
        binding.ivStart.setVisible(item.image?.start != null)
    }

    private fun refreshMargin(binding: ItemTextBinding, item: TextViewItem) {

        binding.root.setMargin(item.margin)
    }

    private fun refreshPadding(binding: ItemTextBinding, item: TextViewItem) {

        binding.tvTitle.setPadding(item.padding)
    }

    private fun refreshTextStyle(binding: ItemTextBinding, item: TextViewItem) {

        binding.tvTitle.setTextStyle(item.textStyle)
    }

    private fun refreshBackground(binding: ItemTextBinding, item: TextViewItem) {

        binding.root.delegate.setBackground(item.background)
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
        text to PAYLOAD_TEXT,
        (size ?: PAYLOAD_SIZE) to PAYLOAD_SIZE,
        (image ?: PAYLOAD_IMAGE) to PAYLOAD_IMAGE,
        (margin ?: PAYLOAD_MARGIN) to PAYLOAD_MARGIN,
        (padding ?: PAYLOAD_PADDING) to PAYLOAD_PADDING,
        (textStyle ?: PAYLOAD_TEXT_STYLE) to PAYLOAD_TEXT_STYLE,
        (background ?: PAYLOAD_BACKGROUND) to PAYLOAD_BACKGROUND
    )

    data class Image(
        val end: Int? = null,
        val start: Int? = null
    )
}

private const val PAYLOAD_TEXT = "PAYLOAD_TEXT"
private const val PAYLOAD_SIZE = "PAYLOAD_SIZE"
private const val PAYLOAD_IMAGE = "PAYLOAD_IMAGE"
private const val PAYLOAD_MARGIN = "PAYLOAD_MARGIN"
private const val PAYLOAD_PADDING = "PAYLOAD_PADDING"
private const val PAYLOAD_TEXT_STYLE = "PAYLOAD_TEXT_STYLE"
private const val PAYLOAD_BACKGROUND = "PAYLOAD_BACKGROUND"