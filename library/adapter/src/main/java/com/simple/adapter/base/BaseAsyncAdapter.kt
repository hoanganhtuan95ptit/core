package com.simple.adapter.base

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.simple.adapter.entities.ViewItem
import com.simple.adapter.utils.DefaultItemCallback
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext


@OptIn(DelicateCoroutinesApi::class)
private val dispatcherForHandleDataUi = newSingleThreadContext("handle_data_for_ui")

abstract class BaseAsyncAdapter<T : ViewItem, B : ViewBinding>(
    itemCallback: DiffUtil.ItemCallback<T>? = null,
) : ListAdapter<T, BaseBindingViewHolder<B>>(
    AsyncDifferConfig
        .Builder(itemCallback ?: DefaultItemCallback<T>())
        .setBackgroundThreadExecutor(dispatcherForHandleDataUi.executor).build()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<B> {

        return BaseBindingViewHolder(createViewBinding(parent, viewType), viewType)
    }

    protected abstract fun createViewBinding(parent: ViewGroup, viewType: Int): B


    override fun onBindViewHolder(holder: BaseBindingViewHolder<B>, position: Int, payloads: MutableList<Any>) {

        val item = getItem(position)
        val binding = holder.binding


        val payload = payloads.flatMap { (it as? List<*>) ?: emptyList() } as? MutableList<*>


        if (!payload.isNullOrEmpty() && item != null) {

            bind(binding, holder.viewType, position, item, payload.filterIsInstance<Any>().toMutableList())
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    protected open fun bind(binding: B, viewType: Int, position: Int, item: T, payloads: MutableList<Any>) {
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<B>, position: Int) {

        val item = getItem(position)
        val binding = holder.binding

        bind(binding, holder.viewType, position, item as T)
    }

    protected open fun bind(binding: B, viewType: Int, position: Int, item: T) {
    }
}