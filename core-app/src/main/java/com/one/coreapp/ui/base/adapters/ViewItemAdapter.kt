package com.one.coreapp.ui.base.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.one.coreapp.utils.extentions.findBinding
import com.one.coreapp.utils.extentions.findGenericClassBySuperClass

abstract class ViewItemAdapter<out VI : ViewItemCloneable, out VB : ViewBinding>(
    private val onItemClick: (View, VI) -> Unit = { _, _ -> }
) {

    var adapter: BaseAsyncAdapter<*, *>? = null

    open fun getViewItemClass(): Class<ViewItemCloneable> {
        return findGenericClassBySuperClass(ViewItemCloneable::class.java)!!.java
    }

    open fun createViewItem(parent: ViewGroup): VB {

        val binding = findBinding<VB>(parent)

        binding.root.setOnClickListener { view ->
            getViewItem<VI>(binding)?.let { onItemClick.invoke(view, it) }
        }

        return binding
    }

    open fun onViewAttachedToWindow(holder: BaseBindingViewHolder<ViewBinding>, adapter: BaseAsyncAdapter<*, *>) {
        this.adapter = adapter
    }

    open fun onViewDetachedFromWindow(holder: BaseBindingViewHolder<ViewBinding>) {
        this.adapter = null
    }

    open fun bind(binding: @UnsafeVariance VB, viewType: Int, position: Int, item: @UnsafeVariance VI, payloads: MutableList<Any>) {
    }

    open fun bind(binding: @UnsafeVariance VB, viewType: Int, position: Int, item: @UnsafeVariance VI) {
    }

    fun <T> getViewItem(binding: ViewBinding): T? {
        return (binding.root.tag as? Int)?.let { adapter?.currentList?.getOrNull(it) as? T }
    }
}

class MultiAdapter(

    vararg adapter: ViewItemAdapter<ViewItemCloneable, ViewBinding>,

    private val onLoadMore: () -> Unit = {},
    private val onViewHolderAttachedToWindow: (BaseBindingViewHolder<*>) -> Unit = {},
    private val onViewHolderDetachedFromWindow: (BaseBindingViewHolder<*>) -> Unit = {},
) : BaseAsyncAdapter<ViewItemCloneable, ViewBinding>(onLoadMore) {

    val list: List<ViewItemAdapter<ViewItemCloneable, ViewBinding>> by lazy {

        val adapters = adapter.toMutableList()

        adapters
    }

    val typeAndAdapter: Map<Int, ViewItemAdapter<ViewItemCloneable, ViewBinding>> by lazy {

        val map = HashMap<Int, ViewItemAdapter<ViewItemCloneable, ViewBinding>>()

        list.forEachIndexed { index, viewItemAdapter ->
            map[index] = viewItemAdapter
        }

        map
    }

    val viewItemClassAndType: Map<Class<*>, Int> by lazy {

        val map = HashMap<Class<*>, Int>()

        list.forEachIndexed { index, viewItemAdapter ->
            map[viewItemAdapter.getViewItemClass()] = index
        }

        map
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        list.forEach { it.adapter = this }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        list.forEach { it.adapter = null }
    }

    override fun getItemViewType(position: Int): Int {

        val viewItem = getItem(position)

        return viewItemClassAndType[viewItem.javaClass] ?: super.getItemViewType(position)
    }

    override fun createView(parent: ViewGroup, viewType: Int?): ViewBinding {

        return typeAndAdapter[viewType]!!.createViewItem(parent)
    }

    override fun onViewAttachedToWindow(holder: BaseBindingViewHolder<ViewBinding>) {
        super.onViewAttachedToWindow(holder)

        onViewHolderAttachedToWindow.invoke(holder)
    }

    override fun onViewDetachedFromWindow(holder: BaseBindingViewHolder<ViewBinding>) {
        super.onViewDetachedFromWindow(holder)

        onViewHolderDetachedFromWindow.invoke(holder)
    }

    override fun bind(binding: ViewBinding, viewType: Int, position: Int, item: ViewItemCloneable, payloads: MutableList<Any>) {
        super.bind(binding, viewType, position, item, payloads)

        typeAndAdapter[viewType]?.bind(binding, viewType, position, item, payloads)
    }

    override fun bind(binding: ViewBinding, viewType: Int, position: Int, item: ViewItemCloneable) {
        super.bind(binding, viewType, position, item)

        typeAndAdapter[viewType]?.bind(binding, viewType, position, item)
    }
}

