package com.one.word.ui.adapters

import com.one.adapter.ViewItemAdapter
import com.one.adapter.ViewItemCloneable
import com.one.coreapp.utils.extentions.setDebouncedClickListener
import com.one.coreapp.utils.extentions.setVisible
import com.one.word.databinding.ItemSpellingBinding
import com.one.word.entities.Spelling

class SpellingAdapter : ViewItemAdapter<SpellingViewItem, ItemSpellingBinding>() {

    override fun bind(binding: ItemSpellingBinding, viewType: Int, position: Int, item: SpellingViewItem) {

        binding.ivSpelling.setDebouncedClickListener {

            getViewItem(position)?.let { binding.ivSpelling.play(it.audio) }
        }

        binding.tvSpelling.setText(item.text)
        binding.ivSpelling.setVisible(item.audio.isNotBlank())

        binding.tvSpellingCode.setText(item.code)
    }
}

data class SpellingViewItem(
    val data: Spelling,
    var code: String = "",
    var text: String = "",
    var audio: String = "",
) : ViewItemCloneable {

    override fun clone() = copy()

    fun refresh() = apply {

        code = data.code.uppercase()
        text = data.text
        audio = data.audio
    }

    override fun areItemsTheSame(): List<Any> = listOf(
        text, audio
    )
}
