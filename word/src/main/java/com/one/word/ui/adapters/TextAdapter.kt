package com.one.word.ui.adapters

import android.graphics.Typeface
import android.widget.TextView
import androidx.core.view.updatePadding
import com.one.adapter.ViewItemAdapter
import com.one.adapter.ViewItemCloneable
import com.one.coreapp.utils.extentions.Text
import com.one.coreapp.utils.extentions.setText
import com.one.word.databinding.ItemTextBinding

class TextAdapter : ViewItemAdapter<TextViewItem, ItemTextBinding>() {

    override fun bind(binding: ItemTextBinding, viewType: Int, position: Int, item: TextViewItem) {

        binding.root.updatePadding(left = item.paddingLeft, top = item.paddingTop)

        binding.tvText.setText(item.text, type = TextView.BufferType.SPANNABLE)
        binding.tvText.setTextSize(item.textSize)

        binding.tvText.setTypeface(binding.tvText.typeface, item.textTypeface)
    }
}

data class TextViewItem(
    val text: Text<*>,

    val textSize: Float = 14f,

    val textTypeface: Int = Typeface.NORMAL,

    val paddingLeft: Int = 0,
    val paddingTop: Int = 0,
) : ViewItemCloneable {

    override fun clone() = copy()

    override fun areItemsTheSame(): List<Any> = listOf(
        text
    )
}
