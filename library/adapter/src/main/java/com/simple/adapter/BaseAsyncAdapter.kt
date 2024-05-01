package com.simple.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.simple.adapter.entities.ViewItem
import com.simple.adapter.utils.DefaultItemCallback
import kotlinx.coroutines.newSingleThreadContext


private val dispatcherForHandleDataUi = newSingleThreadContext("handle_data_for_ui")


abstract class BaseAsyncAdapter<T : ViewItem, B : ViewBinding>(
    itemCallback: DiffUtil.ItemCallback<T>? = null,
) : ListAdapter<T, BaseBindingViewHolder<B>>(
    AsyncDifferConfig
        .Builder(itemCallback ?: DefaultItemCallback<T>())
        .setBackgroundThreadExecutor(dispatcherForHandleDataUi.executor).build()
) {


    open fun setRecyclerView(recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager? = null) {

        recyclerView.adapter = this
        recyclerView.layoutManager = layoutManager ?: getLayoutManager(recyclerView.context)
    }

    open fun getLayoutManager(context: Context): RecyclerView.LayoutManager {

        return LinearLayoutManager(context)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<B> {

        return BaseBindingViewHolder(createView(parent, viewType), viewType)
    }

    protected abstract fun createView(parent: ViewGroup, viewType: Int): B


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


class BaseBindingViewHolder<B : ViewBinding>(val binding: B, val viewType: Int) : RecyclerView.ViewHolder(binding.root)