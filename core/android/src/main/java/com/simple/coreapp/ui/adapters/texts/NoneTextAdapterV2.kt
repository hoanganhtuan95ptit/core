package com.simple.coreapp.ui.adapters.texts

import android.view.ViewGroup
import com.simple.coreapp.databinding.ItemTextBinding
import com.simple.coreapp.ui.view.Background
import com.simple.coreapp.ui.view.Margin
import com.simple.coreapp.ui.view.Padding
import com.simple.coreapp.ui.view.Size
import com.simple.coreapp.ui.view.TextStyle
import com.tuanha.adapter.annotation.ItemAdapter

@ItemAdapter
class NoneTextAdapterV2 : com.tuanha.adapter.ViewItemAdapter<NoneTextViewItem, ItemTextBinding>(), TextAdapterV2 {

    override val viewItemClass: Class<NoneTextViewItem> by lazy {
        NoneTextViewItem::class.java
    }

    override fun createViewBinding(parent: ViewGroup, viewType: Int): ItemTextBinding {
        return createBinding(parent, viewType)
    }

    override fun onBindViewHolder(binding: ItemTextBinding, viewType: Int, position: Int, item: NoneTextViewItem, payloads: MutableList<Any>) {

        binding(binding, viewType, position, item as TextViewItem, payloads)
    }

    override fun onBindViewHolder(binding: ItemTextBinding, viewType: Int, position: Int, item: NoneTextViewItem) {

        binding(binding, viewType, position, item as TextViewItem)
    }
}

data class NoneTextViewItem(
    override val id: String = "",
    override val data: Any? = null,

    override val size: Size? = null,
    override val margin: Margin? = null,
    override val padding: Padding? = null,
    override var background: Background? = null,


    override var text: CharSequence = "",
    override var textStyle: TextStyle? = null,
    override val textSize: Size? = null,
    override val textMargin: Margin? = null,
    override val textPadding: Padding? = null,
    override var textBackground: Background? = null,


    override val imageLeft: Int? = null,
    override val imageLeftSize: Size? = null,
    override val imageLeftMargin: Margin? = null,
    override val imageLeftPadding: Padding? = null,
    override var imageLeftBackground: Background? = null,


    override val imageRight: Int? = null,
    override val imageRightSize: Size? = null,
    override val imageRightMargin: Margin? = null,
    override val imageRightPadding: Padding? = null,
    override var imageRightBackground: Background? = null,
) : TextViewItem()