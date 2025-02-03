package com.simple.coreapp.ui.adapters.texts

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

internal interface TextAdapter {

    fun binding(binding: ItemTextBinding, viewType: Int, position: Int, item: TextViewItem, payloads: MutableList<Any>) {

        if (payloads.contains(PAYLOAD_TEXT)) refreshText(binding, item)
        if (payloads.contains(PAYLOAD_TEXT_STYLE)) refreshTextStyle(binding, item)

        if (payloads.contains(PAYLOAD_IMAGE)) refreshImage(binding, item)

        if (payloads.contains(PAYLOAD_SIZE)) refreshSize(binding, item)
        if (payloads.contains(PAYLOAD_MARGIN)) refreshMargin(binding, item)
        if (payloads.contains(PAYLOAD_PADDING)) refreshPadding(binding, item)
        if (payloads.contains(PAYLOAD_BACKGROUND)) refreshBackground(binding, item)

        if (payloads.contains(PAYLOAD_TEXT_SIZE)) refreshTextSize(binding, item)
        if (payloads.contains(PAYLOAD_TEXT_MARGIN)) refreshTextMargin(binding, item)
        if (payloads.contains(PAYLOAD_TEXT_PADDING)) refreshTextPadding(binding, item)
        if (payloads.contains(PAYLOAD_TEXT_BACKGROUND)) refreshTextBackground(binding, item)
    }

    fun binding(binding: ItemTextBinding, viewType: Int, position: Int, item: TextViewItem) {

        refreshText(binding, item)
        refreshTextStyle(binding, item)

        refreshImage(binding, item)

        refreshSize(binding, item)
        refreshMargin(binding, item)
        refreshPadding(binding, item)
        refreshBackground(binding, item)

        refreshTextSize(binding, item)
        refreshTextMargin(binding, item)
        refreshTextPadding(binding, item)
        refreshTextBackground(binding, item)
    }

    private fun refreshText(binding: ItemTextBinding, item: TextViewItem) {

        binding.tvTitle.text = item.text
    }

    private fun refreshTextStyle(binding: ItemTextBinding, item: TextViewItem) {

        binding.tvTitle.setTextStyle(item.textStyle)
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

    private fun refreshSize(binding: ItemTextBinding, item: TextViewItem) {

        binding.root.setSize(item.size)
    }

    private fun refreshMargin(binding: ItemTextBinding, item: TextViewItem) {

        binding.root.setMargin(item.margin)
    }

    private fun refreshPadding(binding: ItemTextBinding, item: TextViewItem) {

        binding.root.setPadding(item.padding)
    }

    private fun refreshBackground(binding: ItemTextBinding, item: TextViewItem) {

        binding.root.delegate.setBackground(item.background)
    }

    private fun refreshTextSize(binding: ItemTextBinding, item: TextViewItem) {

        binding.tvTitle.setSize(item.textSize)
    }

    private fun refreshTextMargin(binding: ItemTextBinding, item: TextViewItem) {

        binding.tvTitle.setMargin(item.textMargin)
    }

    private fun refreshTextPadding(binding: ItemTextBinding, item: TextViewItem) {

        binding.tvTitle.setPadding(item.textPadding)
    }

    private fun refreshTextBackground(binding: ItemTextBinding, item: TextViewItem) {

        binding.tvTitle.delegate.setBackground(item.textBackground)
    }
}

open class TextViewItem : ViewItem {

    open val id: String = ""

    open val data: Any? = null

    open val image: Image? = null

    open var text: CharSequence = ""
    open var textStyle: TextStyle? = null

    open val size: Size? = null
    open val margin: Margin? = null
    open val padding: Padding? = null
    open var background: Background? = null

    open val textSize: Size? = null
    open val textMargin: Margin? = null
    open val textPadding: Padding? = null
    open var textBackground: Background? = null

    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )

    override fun getContentsCompare(): List<Pair<Any, String>> = listOf(
        text to PAYLOAD_TEXT,
        (textStyle ?: PAYLOAD_TEXT_STYLE) to PAYLOAD_TEXT_STYLE,

        (image ?: PAYLOAD_IMAGE) to PAYLOAD_IMAGE,

        (size ?: PAYLOAD_SIZE) to PAYLOAD_SIZE,
        (margin ?: PAYLOAD_MARGIN) to PAYLOAD_MARGIN,
        (padding ?: PAYLOAD_PADDING) to PAYLOAD_PADDING,
        (background ?: PAYLOAD_BACKGROUND) to PAYLOAD_BACKGROUND,

        (textSize ?: PAYLOAD_TEXT_SIZE) to PAYLOAD_TEXT_SIZE,
        (textMargin ?: PAYLOAD_TEXT_MARGIN) to PAYLOAD_TEXT_MARGIN,
        (textPadding ?: PAYLOAD_TEXT_PADDING) to PAYLOAD_TEXT_PADDING,
        (textBackground ?: PAYLOAD_TEXT_BACKGROUND) to PAYLOAD_TEXT_BACKGROUND
    )

    data class Image(
        val end: Int? = null,
        val start: Int? = null
    )
}

private const val PAYLOAD_TEXT = "PAYLOAD_TEXT"
private const val PAYLOAD_TEXT_STYLE = "PAYLOAD_TEXT_STYLE"

private const val PAYLOAD_IMAGE = "PAYLOAD_IMAGE"

private const val PAYLOAD_SIZE = "PAYLOAD_SIZE"
private const val PAYLOAD_MARGIN = "PAYLOAD_MARGIN"
private const val PAYLOAD_PADDING = "PAYLOAD_PADDING"
private const val PAYLOAD_BACKGROUND = "PAYLOAD_BACKGROUND"

private const val PAYLOAD_TEXT_SIZE = "PAYLOAD_TEXT_SIZE"
private const val PAYLOAD_TEXT_MARGIN = "PAYLOAD_TEXT_MARGIN"
private const val PAYLOAD_TEXT_PADDING = "PAYLOAD_TEXT_PADDING"
private const val PAYLOAD_TEXT_BACKGROUND = "PAYLOAD_TEXT_BACKGROUND"