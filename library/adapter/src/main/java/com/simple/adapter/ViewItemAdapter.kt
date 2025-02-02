package com.simple.adapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.simple.adapter.entities.ViewItem
import com.simple.binding.findBinding
import com.simple.core.utils.extentions.findGenericClassBySuperClass

abstract class ViewItemAdapter<out VI : ViewItem, out VB : ViewBinding>(
    private val onItemClick: ((View, VI) -> Unit)? = null
) {


    var adapter: BaseAsyncAdapter<*, *>? = null


    val viewItemClass: Class<ViewItem> by lazy {

        this.findGenericClassBySuperClass(ViewItem::class.java)!!.java
    }


    private val viewBindingClass: Class<ViewBinding> by lazy {

        findGenericClassBySuperClass(ViewBinding::class.java)!!.java
    }


    @CallSuper
    open fun onAttachedToRecyclerView(recyclerView: RecyclerView) {

    }

    @CallSuper
    open fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {

    }


    open fun createViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<@UnsafeVariance VB>? {

        val viewHolder = BaseBindingViewHolder(createViewItem(parent, viewType), viewType)

        val binding = viewHolder.binding

        val onItemClick = onItemClick ?: return viewHolder

        binding.root.setOnClickListener { view ->

            getViewItem(viewHolder.bindingAdapterPosition)?.let { onItemClick.invoke(view, it) }
        }

        return viewHolder
    }

    open fun createViewItem(parent: ViewGroup, viewType: Int): VB {

        return viewBindingClass.findBinding(parent)
    }


    @CallSuper
    open fun onViewAttachedToWindow(holder: BaseBindingViewHolder<ViewBinding>) {
    }

    @CallSuper
    open fun onViewDetachedFromWindow(holder: BaseBindingViewHolder<ViewBinding>) {
    }


    open fun bind(binding: @UnsafeVariance VB, viewType: Int, position: Int, item: @UnsafeVariance VI, payloads: MutableList<Any>) {
    }

    open fun bind(binding: @UnsafeVariance VB, viewType: Int, position: Int, item: @UnsafeVariance VI) {
    }


    protected fun getViewItem(position: Int) = adapter?.currentList?.getOrNull(position) as? VI
}