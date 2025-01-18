package com.simple.coreapp.ui.adapters

import android.text.InputType
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.simple.adapter.BaseBindingViewHolder
import com.simple.adapter.ViewItemAdapter
import com.simple.adapter.entities.ViewItem
import com.simple.coreapp.databinding.ItemInputBinding
import com.simple.coreapp.ui.view.round.Background
import com.simple.coreapp.ui.view.round.setBackground

open class InputAdapter(
    private val onInputFocus: (View, InputViewItem) -> Unit = { _, _ -> },
    private val onInputChange: (View, InputViewItem) -> Unit = { _, _ -> }
) : ViewItemAdapter<InputViewItem, ItemInputBinding>() {

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

    override fun bind(binding: ItemInputBinding, viewType: Int, position: Int, item: InputViewItem, payloads: MutableList<Any>) {
        super.bind(binding, viewType, position, item, payloads)

        if (payloads.contains(PAYLOAD_TEXT)) {

            refreshText(binding, item)
        }
    }

    override fun bind(binding: ItemInputBinding, viewType: Int, position: Int, item: InputViewItem) {
        super.bind(binding, viewType, position, item)

        binding.edtName.hint = item.hint

        binding.edtName.setInputType(item.inputType)

        binding.root.delegate.setBackground(item.background)

        refreshText(binding, item)
    }

    private fun refreshText(binding: ItemInputBinding, item: InputViewItem) {

        binding.edtName.setText(item.text)
    }
}

class InputViewItem(
    val id: String = "",

    val hint: CharSequence = "",
    var text: CharSequence = "",

    val inputType: Int = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS,

    var background: Background? = null,
) : ViewItem {

    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )

    override fun getContentsCompare(): List<Pair<Any, String>> = listOf(
        text to PAYLOAD_TEXT
    )
}

private const val PAYLOAD_TEXT = "PAYLOAD_TEXT"