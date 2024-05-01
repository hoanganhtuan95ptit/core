package com.simple.adapter.utils

import androidx.recyclerview.widget.DiffUtil
import com.simple.adapter.entities.ViewItem

internal class DefaultItemCallback<T : ViewItem> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {

        return oldItem.javaClass.name == newItem.javaClass.name && oldItem.areItemsTheSame() == newItem.areItemsTheSame()
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {

        val payloads = ArrayList<Any>()

        val oldItemCompare = oldItem.getContentsCompare()
        val newItemCompare = newItem.getContentsCompare()

        oldItemCompare.forEachIndexed { index, pair ->

            if (pair.first != newItemCompare.getOrNull(index)?.first) payloads.add(pair.second)
        }

        return payloads.isEmpty()
    }

    override fun getChangePayload(oldItem: T, newItem: T): Any? {
        val payloads = ArrayList<Any>()

        val oldItemCompare = oldItem.getContentsCompare()
        val newItemCompare = newItem.getContentsCompare()

        oldItemCompare.forEachIndexed { index, pair ->
            if (pair.first != newItemCompare.getOrNull(index)?.first) payloads.add(pair.second)
        }

        payloads.add(Int.MAX_VALUE)

        return if (payloads.size > 0) {
            payloads
        } else {
            super.getChangePayload(oldItem, newItem)
        }
    }
}
