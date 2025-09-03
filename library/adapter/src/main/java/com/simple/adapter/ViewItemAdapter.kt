package com.simple.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.simple.adapter.base.BaseBindingViewHolder
import com.simple.adapter.entities.ViewItem

@Suppress("UNCHECKED_CAST")
abstract class ViewItemAdapter<out VI : ViewItem, out VB : ViewBinding>(private val onItemClick: ((View, VI) -> Unit)? = null) {


    open var adapter: MultiAdapter? = null


    abstract val viewItemClass: Class<@UnsafeVariance VI>

    abstract fun createViewBinding(parent: ViewGroup, viewType: Int): VB


    open fun createViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<@UnsafeVariance VB>? {

        val viewHolder = BaseBindingViewHolder(createViewBinding(parent, viewType), viewType)

        val binding = viewHolder.binding

        val onItemClick = onItemClick ?: return viewHolder

        binding.root.setOnClickListener { view ->

            getViewItem(viewHolder.bindingAdapterPosition)?.let { onItemClick.invoke(view, it) }
        }

        return viewHolder
    }

    open fun onViewAttachedToWindow(holder: BaseBindingViewHolder<ViewBinding>) {
    }

    open fun onViewDetachedFromWindow(holder: BaseBindingViewHolder<ViewBinding>) {
    }

    open fun onBindViewHolder(binding: @UnsafeVariance VB, viewType: Int, position: Int, item: @UnsafeVariance VI, payloads: MutableList<Any>) {
    }

    open fun onBindViewHolder(binding: @UnsafeVariance VB, viewType: Int, position: Int, item: @UnsafeVariance VI) {
    }

    protected fun getViewItem(position: Int) = adapter?.currentList?.getOrNull(position) as? VI
}