package com.simple.coreapp.ui.adapters.texts

import android.view.ViewGroup
import com.simple.coreapp.databinding.ItemTextBinding
import com.simple.coreapp.ui.view.Background
import com.simple.coreapp.ui.view.Margin
import com.simple.coreapp.ui.view.Padding
import com.simple.coreapp.ui.view.Size
import com.simple.coreapp.ui.view.TextStyle
import com.simple.coreapp.utils.ext.setDebouncedClickListener
import com.tuanha.adapter.annotation.ItemAdapter
import com.tuanha.adapter.base.BaseBindingViewHolder
import com.tuanha.event.sendEvent

@ItemAdapter
class ClickTextAdapterV2() : com.tuanha.adapter.ViewItemAdapter<ClickTextViewItemV2V2, ItemTextBinding>(), TextAdapterV2 {

    override val viewItemClass: Class<ClickTextViewItemV2V2> by lazy {
        ClickTextViewItemV2V2::class.java
    }

    override fun createViewBinding(parent: ViewGroup, viewType: Int): ItemTextBinding {
        return createBinding(parent, viewType)
    }

    override fun createViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ItemTextBinding>? {
        val viewHolder = super.createViewHolder(parent, viewType) ?: return null

        viewHolder.binding.root.setDebouncedClickListener {

            val item = getViewItem(viewHolder.absoluteAdapterPosition) ?: return@setDebouncedClickListener
            sendEvent("CLICK_TEXT", item)
        }

        return viewHolder
    }

    override fun onBindViewHolder(binding: ItemTextBinding, viewType: Int, position: Int, item: ClickTextViewItemV2V2, payloads: MutableList<Any>) {

        binding(binding, viewType, position, item as TextViewItemV2, payloads)
    }

    override fun onBindViewHolder(binding: ItemTextBinding, viewType: Int, position: Int, item: ClickTextViewItemV2V2) {

        binding(binding, viewType, position, item as TextViewItemV2)
    }
}

data class ClickTextViewItemV2V2(
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
) : TextViewItemV2()