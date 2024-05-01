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
    private val onItemClick: (View, VI) -> Unit = { _, _ -> }
) {


    var adapter: BaseAsyncAdapter<*, *>? = null

    val transitionName: String by lazy {

        this.javaClass.name
    }

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


    open fun onBindViewHolder(holder: BaseBindingViewHolder<ViewBinding>, binding: @UnsafeVariance VB, position: Int) {

        binding.root.transitionName = "$transitionName-$position"
    }


    fun bindView(binding: @UnsafeVariance VB, viewType: Int, position: Int, item: @UnsafeVariance VI, payloads: MutableList<Any>) {

        bind(binding, viewType, position, item, payloads)
    }

    protected open fun bind(binding: @UnsafeVariance VB, viewType: Int, position: Int, item: @UnsafeVariance VI, payloads: MutableList<Any>) {
    }


    fun bindView(binding: @UnsafeVariance VB, viewType: Int, position: Int, item: @UnsafeVariance VI) {

        bind(binding, viewType, position, item)
    }

    protected open fun bind(binding: @UnsafeVariance VB, viewType: Int, position: Int, item: @UnsafeVariance VI) {
    }


    protected fun getViewItem(position: Int) = adapter?.currentList?.getOrNull(position) as? VI
}