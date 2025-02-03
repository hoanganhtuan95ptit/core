package com.simple.coreapp.ui.adapters.texts

import android.view.View
import com.simple.adapter.ViewItemAdapter
import com.simple.coreapp.databinding.ItemTextBinding
import com.simple.coreapp.ui.view.Margin
import com.simple.coreapp.ui.view.Padding
import com.simple.coreapp.ui.view.Size
import com.simple.coreapp.ui.view.TextStyle
import com.simple.coreapp.ui.view.round.Background

open class ClickTextAdapter(onItemClick: ((View, ClickTextViewItem) -> Unit)? = null) : ViewItemAdapter<ClickTextViewItem, ItemTextBinding>(onItemClick), TextAdapter {

    override fun bind(binding: ItemTextBinding, viewType: Int, position: Int, item: ClickTextViewItem, payloads: MutableList<Any>) {

        binding(binding, viewType, position, item as TextViewItem, payloads)
    }

    override fun bind(binding: ItemTextBinding, viewType: Int, position: Int, item: ClickTextViewItem) {

        binding(binding, viewType, position, item as TextViewItem)
    }
}

data class ClickTextViewItem(
    override val id: String = "",
    override val data: Any? = null,

    override var text: CharSequence = "",
    override var textStyle: TextStyle? = null,

    override val image: Image? = null,

    override val size: Size? = null,
    override val margin: Margin? = null,
    override val padding: Padding? = null,
    override var background: Background? = null,

    override val textSize: Size? = null,
    override val textMargin: Margin? = null,
    override val textPadding: Padding? = null,
    override var textBackground: Background? = null
) : TextViewItem()