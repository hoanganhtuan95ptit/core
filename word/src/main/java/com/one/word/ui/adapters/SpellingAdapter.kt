package com.one.word.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.one.coreapp.ui.base.adapters.ViewItemAdapter
import com.one.coreapp.ui.base.adapters.ViewItemCloneable
import com.one.coreapp.utils.extentions.setDebouncedClickListener
import com.one.coreapp.utils.extentions.setVisible
import com.one.word.databinding.ItemSpellingBinding
import com.one.word.entities.Spelling

class SpellingAdapter : ViewItemAdapter<SpellingViewItem, ItemSpellingBinding>() {

    override fun createViewItem(parent: ViewGroup): ItemSpellingBinding {

        val binding = ItemSpellingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.ivSpelling.setDebouncedClickListener {

            adapter?.getViewItem<SpellingViewItem>(binding)?.let { binding.ivSpelling.play(it.audio) }
        }

        return binding
    }

    override fun bind(binding: ItemSpellingBinding, viewType: Int, position: Int, item: SpellingViewItem) {

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
