package com.simple.coreapp.ui.adapters.texts

import android.view.LayoutInflater
import android.view.ViewGroup
import com.simple.coreapp.databinding.ItemTextBinding
import com.simple.coreapp.ui.view.Background
import com.simple.coreapp.ui.view.Margin
import com.simple.coreapp.ui.view.Padding
import com.simple.coreapp.ui.view.Size
import com.simple.coreapp.ui.view.TextStyle
import com.simple.coreapp.ui.view.setBackground
import com.simple.coreapp.ui.view.setMargin
import com.simple.coreapp.ui.view.setPadding
import com.simple.coreapp.ui.view.setSize
import com.simple.coreapp.ui.view.setTextStyle
import com.simple.coreapp.utils.ext.setVisible
import com.simple.image.setImage

internal interface TextAdapter {

    fun createBinding(parent: ViewGroup, viewType: Int): ItemTextBinding {
        return ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    fun binding(binding: ItemTextBinding, viewType: Int, position: Int, item: TextViewItem, payloads: MutableList<Any>) {

        binding.root.transitionName = item.id

        if (payloads.contains(PAYLOAD_SIZE)) refreshSize(binding, item)
        if (payloads.contains(PAYLOAD_MARGIN)) refreshMargin(binding, item)
        if (payloads.contains(PAYLOAD_PADDING)) refreshPadding(binding, item)
        if (payloads.contains(PAYLOAD_BACKGROUND)) refreshBackground(binding, item)

        if (payloads.contains(PAYLOAD_TEXT)) refreshText(binding, item)
        if (payloads.contains(PAYLOAD_TEXT_STYLE)) refreshTextStyle(binding, item)
        if (payloads.contains(PAYLOAD_TEXT_SIZE)) refreshTextSize(binding, item)
        if (payloads.contains(PAYLOAD_TEXT_MARGIN)) refreshTextMargin(binding, item)
        if (payloads.contains(PAYLOAD_TEXT_PADDING)) refreshTextPadding(binding, item)
        if (payloads.contains(PAYLOAD_TEXT_BACKGROUND)) refreshTextBackground(binding, item)

        if (payloads.contains(PAYLOAD_IMAGE_LEFT)) refreshImageLeft(binding, item)
        if (payloads.contains(PAYLOAD_IMAGE_LEFT_SIZE)) refreshImageLeftSize(binding, item)
        if (payloads.contains(PAYLOAD_IMAGE_LEFT_MARGIN)) refreshImageLeftMargin(binding, item)
        if (payloads.contains(PAYLOAD_IMAGE_LEFT_PADDING)) refreshImageLeftPadding(binding, item)
        if (payloads.contains(PAYLOAD_IMAGE_LEFT_BACKGROUND)) refreshImageLeftBackground(binding, item)

        if (payloads.contains(PAYLOAD_IMAGE_RIGHT)) refreshImageRight(binding, item)
        if (payloads.contains(PAYLOAD_IMAGE_RIGHT_SIZE)) refreshImageRightSize(binding, item)
        if (payloads.contains(PAYLOAD_IMAGE_RIGHT_MARGIN)) refreshImageRightMargin(binding, item)
        if (payloads.contains(PAYLOAD_IMAGE_RIGHT_PADDING)) refreshImageRightPadding(binding, item)
        if (payloads.contains(PAYLOAD_IMAGE_RIGHT_BACKGROUND)) refreshImageRightBackground(binding, item)
    }

    fun binding(binding: ItemTextBinding, viewType: Int, position: Int, item: TextViewItem) {

        binding.root.transitionName = item.id

        refreshSize(binding, item)
        refreshMargin(binding, item)
        refreshPadding(binding, item)
        refreshBackground(binding, item)

        refreshText(binding, item)
        refreshTextStyle(binding, item)
        refreshTextSize(binding, item)
        refreshTextMargin(binding, item)
        refreshTextPadding(binding, item)
        refreshTextBackground(binding, item)

        refreshImageLeft(binding, item)
        refreshImageLeftSize(binding, item)
        refreshImageLeftMargin(binding, item)
        refreshImageLeftPadding(binding, item)
        refreshImageLeftBackground(binding, item)

        refreshImageRight(binding, item)
        refreshImageRightSize(binding, item)
        refreshImageRightMargin(binding, item)
        refreshImageRightPadding(binding, item)
        refreshImageRightBackground(binding, item)
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


    private fun refreshText(binding: ItemTextBinding, item: TextViewItem) {

        binding.tvTitle.text = item.text
    }

    private fun refreshTextStyle(binding: ItemTextBinding, item: TextViewItem) {

        binding.tvTitle.setTextStyle(item.textStyle)
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


    private fun refreshImageLeft(binding: ItemTextBinding, item: TextViewItem) {

        binding.ivLeft.setVisible(item.imageLeft != null)
        binding.ivLeft.setImage(item.imageLeft ?: return)
    }

    private fun refreshImageLeftSize(binding: ItemTextBinding, item: TextViewItem) {

        binding.ivLeft.setSize(item.imageLeftSize)
    }

    private fun refreshImageLeftMargin(binding: ItemTextBinding, item: TextViewItem) {

        binding.ivLeft.setMargin(item.imageLeftMargin)
    }

    private fun refreshImageLeftPadding(binding: ItemTextBinding, item: TextViewItem) {

        binding.ivLeft.setPadding(item.imageLeftPadding)
    }

    private fun refreshImageLeftBackground(binding: ItemTextBinding, item: TextViewItem) {

        binding.ivLeft.delegate.setBackground(item.imageLeftBackground)
    }


    private fun refreshImageRight(binding: ItemTextBinding, item: TextViewItem) {

        binding.ivRight.setVisible(item.imageRight != null)
        binding.ivRight.setImage(item.imageRight ?: return)
    }

    private fun refreshImageRightSize(binding: ItemTextBinding, item: TextViewItem) {

        binding.ivRight.setSize(item.imageRightSize)
    }

    private fun refreshImageRightMargin(binding: ItemTextBinding, item: TextViewItem) {

        binding.ivRight.setMargin(item.imageRightMargin)
    }

    private fun refreshImageRightPadding(binding: ItemTextBinding, item: TextViewItem) {

        binding.ivRight.setPadding(item.imageRightPadding)
    }

    private fun refreshImageRightBackground(binding: ItemTextBinding, item: TextViewItem) {

        binding.ivRight.delegate.setBackground(item.imageRightBackground)
    }
}

open class TextViewItem : com.simple.adapter.entities.ViewItem {

    open val id: String = ""

    open val data: Any? = null

    open val size: Size? = null
    open val margin: Margin? = null
    open val padding: Padding? = null
    open var background: Background? = null


    open var text: CharSequence = ""
    open var textStyle: TextStyle? = null

    open val textSize: Size? = null
    open val textMargin: Margin? = null
    open val textPadding: Padding? = null
    open var textBackground: Background? = null


    open val imageLeft: Int? = null

    open val imageLeftSize: Size? = null
    open val imageLeftMargin: Margin? = null
    open val imageLeftPadding: Padding? = null
    open var imageLeftBackground: Background? = null


    open val imageRight: Int? = null

    open val imageRightSize: Size? = null
    open val imageRightMargin: Margin? = null
    open val imageRightPadding: Padding? = null
    open var imageRightBackground: Background? = null


    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )

    override fun getContentsCompare(): List<Pair<Any, String>> = listOf(
        (size ?: PAYLOAD_SIZE) to PAYLOAD_SIZE,
        (margin ?: PAYLOAD_MARGIN) to PAYLOAD_MARGIN,
        (padding ?: PAYLOAD_PADDING) to PAYLOAD_PADDING,
        (background ?: PAYLOAD_BACKGROUND) to PAYLOAD_BACKGROUND,


        text to PAYLOAD_TEXT,
        (textStyle ?: PAYLOAD_TEXT_STYLE) to PAYLOAD_TEXT_STYLE,
        (textSize ?: PAYLOAD_TEXT_SIZE) to PAYLOAD_TEXT_SIZE,
        (textMargin ?: PAYLOAD_TEXT_MARGIN) to PAYLOAD_TEXT_MARGIN,
        (textPadding ?: PAYLOAD_TEXT_PADDING) to PAYLOAD_TEXT_PADDING,
        (textBackground ?: PAYLOAD_TEXT_BACKGROUND) to PAYLOAD_TEXT_BACKGROUND,


        (imageLeft ?: PAYLOAD_IMAGE_LEFT) to PAYLOAD_IMAGE_LEFT,
        (imageLeftSize ?: PAYLOAD_IMAGE_LEFT_SIZE) to PAYLOAD_IMAGE_LEFT_SIZE,
        (imageLeftMargin ?: PAYLOAD_IMAGE_LEFT_MARGIN) to PAYLOAD_IMAGE_LEFT_MARGIN,
        (imageLeftPadding ?: PAYLOAD_IMAGE_LEFT_PADDING) to PAYLOAD_IMAGE_LEFT_PADDING,
        (imageLeftBackground ?: PAYLOAD_IMAGE_LEFT_BACKGROUND) to PAYLOAD_IMAGE_LEFT_BACKGROUND,


        (imageRight ?: PAYLOAD_IMAGE_RIGHT) to PAYLOAD_IMAGE_RIGHT,
        (imageRightSize ?: PAYLOAD_IMAGE_RIGHT_SIZE) to PAYLOAD_IMAGE_RIGHT_SIZE,
        (imageRightMargin ?: PAYLOAD_IMAGE_RIGHT_MARGIN) to PAYLOAD_IMAGE_RIGHT_MARGIN,
        (imageRightPadding ?: PAYLOAD_IMAGE_RIGHT_PADDING) to PAYLOAD_IMAGE_RIGHT_PADDING,
        (imageRightBackground ?: PAYLOAD_IMAGE_RIGHT_BACKGROUND) to PAYLOAD_IMAGE_RIGHT_BACKGROUND
    )
}

private const val PAYLOAD_SIZE = "PAYLOAD_SIZE"
private const val PAYLOAD_MARGIN = "PAYLOAD_MARGIN"
private const val PAYLOAD_PADDING = "PAYLOAD_PADDING"
private const val PAYLOAD_BACKGROUND = "PAYLOAD_BACKGROUND"


private const val PAYLOAD_TEXT = "PAYLOAD_TEXT"
private const val PAYLOAD_TEXT_STYLE = "PAYLOAD_TEXT_STYLE"

private const val PAYLOAD_TEXT_SIZE = "PAYLOAD_TEXT_SIZE"
private const val PAYLOAD_TEXT_MARGIN = "PAYLOAD_TEXT_MARGIN"
private const val PAYLOAD_TEXT_PADDING = "PAYLOAD_TEXT_PADDING"
private const val PAYLOAD_TEXT_BACKGROUND = "PAYLOAD_TEXT_BACKGROUND"


private const val PAYLOAD_IMAGE_LEFT = "PAYLOAD_IMAGE_LEFT"

private const val PAYLOAD_IMAGE_LEFT_SIZE = "PAYLOAD_IMAGE_LEFT_SIZE"
private const val PAYLOAD_IMAGE_LEFT_MARGIN = "PAYLOAD_IMAGE_LEFT_MARGIN"
private const val PAYLOAD_IMAGE_LEFT_PADDING = "PAYLOAD_IMAGE_LEFT_PADDING"
private const val PAYLOAD_IMAGE_LEFT_BACKGROUND = "PAYLOAD_IMAGE_LEFT_BACKGROUND"


private const val PAYLOAD_IMAGE_RIGHT = "PAYLOAD_IMAGE_RIGHT"

private const val PAYLOAD_IMAGE_RIGHT_SIZE = "PAYLOAD_IMAGE_RIGHT_SIZE"
private const val PAYLOAD_IMAGE_RIGHT_MARGIN = "PAYLOAD_IMAGE_RIGHT_MARGIN"
private const val PAYLOAD_IMAGE_RIGHT_PADDING = "PAYLOAD_IMAGE_RIGHT_PADDING"
private const val PAYLOAD_IMAGE_RIGHT_BACKGROUND = "PAYLOAD_IMAGE_RIGHT_BACKGROUND"

