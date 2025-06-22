package com.simple.coreapp.ui.adapters

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.simple.adapter.ViewItemAdapter
import com.simple.adapter.annotation.ItemAdapter
import com.simple.adapter.base.BaseBindingViewHolder
import com.simple.adapter.entities.ViewItem
import com.simple.coreapp.databinding.ItemInputBinding
import com.simple.coreapp.ui.view.Background
import com.simple.coreapp.ui.view.TextStyle
import com.simple.coreapp.ui.view.setBackground
import com.simple.coreapp.ui.view.setTextStyle


@ItemAdapter
class InputAdapter(
    private val onInputFocus: (View, InputViewItem) -> Unit = { _, _ -> },
    private val onInputChange: (View, InputViewItem) -> Unit = { _, _ -> }
) : ViewItemAdapter<InputViewItem, ItemInputBinding>() {

    override val viewItemClass: Class<InputViewItem> by lazy {
        InputViewItem::class.java
    }

    override fun createViewBinding(parent: ViewGroup, viewType: Int): ItemInputBinding {
        return ItemInputBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun createViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ItemInputBinding>? {

        val holder = super.createViewHolder(parent, viewType) ?: return null

        holder.binding.edtName.setOnFocusChangeListener { v, hasFocus ->

            if (!hasFocus) return@setOnFocusChangeListener

            val item = getViewItem(holder.bindingAdapterPosition) ?: return@setOnFocusChangeListener

            onInputFocus(holder.binding.edtName, item)

            holder.binding.edtName.selectAll()
        }

        holder.binding.edtName.doAfterTextChanged {

            val item = getViewItem(holder.bindingAdapterPosition) ?: return@doAfterTextChanged
            if (!holder.binding.edtName.isFocused) return@doAfterTextChanged

            item.text = it.toString()

            onInputChange(holder.binding.edtName, item)
        }

        return holder
    }

    override fun onBindViewHolder(binding: ItemInputBinding, viewType: Int, position: Int, item: InputViewItem, payloads: MutableList<Any>) {
        super.onBindViewHolder(binding, viewType, position, item, payloads)

        if (payloads.contains(PAYLOAD_TEXT)) refreshText(binding, item)
        if (payloads.contains(PAYLOAD_HINT)) refreshHint(binding, item)
        if (payloads.contains(PAYLOAD_TEXT_STYLE)) refreshTextStyle(binding, item)
        if (payloads.contains(PAYLOAD_INPUT_TYPE)) refreshInputType(binding, item)
        if (payloads.contains(PAYLOAD_BACKGROUND)) refreshBackground(binding, item)
    }

    override fun onBindViewHolder(binding: ItemInputBinding, viewType: Int, position: Int, item: InputViewItem) {
        super.onBindViewHolder(binding, viewType, position, item)

        refreshText(binding, item)
        refreshHint(binding, item)
        refreshTextStyle(binding, item)
        refreshInputType(binding, item)
        refreshBackground(binding, item)
    }

    private fun refreshHint(binding: ItemInputBinding, item: InputViewItem) {

        binding.edtName.hint = item.hint
    }

    private fun refreshText(binding: ItemInputBinding, item: InputViewItem) {

        binding.edtName.setText(item.text)
    }

    private fun refreshTextStyle(binding: ItemInputBinding, item: InputViewItem) {

        binding.edtName.setTextStyle(item.textStyle)
    }

    private fun refreshInputType(binding: ItemInputBinding, item: InputViewItem) {

        binding.edtName.setInputType(item.inputType)
    }

    private fun refreshBackground(binding: ItemInputBinding, item: InputViewItem) {

        binding.root.delegate.setBackground(item.background)
    }
}

data class InputViewItem(
    val id: String = "",

    val hint: CharSequence = "",
    var text: CharSequence = "",

    val inputType: Int = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS,

    val textStyle: TextStyle? = null,
    var background: Background? = null,
) : ViewItem {

    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )

    override fun getContentsCompare(): List<Pair<Any, String>> = listOf(
        hint to PAYLOAD_HINT,
        text to PAYLOAD_TEXT,
        inputType to PAYLOAD_INPUT_TYPE,
        (background ?: PAYLOAD_BACKGROUND) to PAYLOAD_BACKGROUND,
        (textStyle ?: PAYLOAD_TEXT_STYLE) to PAYLOAD_TEXT_STYLE,
    )
}

private const val PAYLOAD_HINT = "PAYLOAD_HINT"
private const val PAYLOAD_TEXT = "PAYLOAD_TEXT"
private const val PAYLOAD_INPUT_TYPE = "PAYLOAD_INPUT_TYPE"
private const val PAYLOAD_BACKGROUND = "PAYLOAD_BACKGROUND"
private const val PAYLOAD_TEXT_STYLE = "PAYLOAD_TEXT_STYLE"
