package com.simple.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.simple.adapter.base.BaseAsyncAdapter
import com.simple.adapter.base.BaseBindingViewHolder
import com.simple.adapter.entities.ViewItem

class MultiAdapter(
    vararg adapter: ViewItemAdapter<ViewItem, ViewBinding>,
) : BaseAsyncAdapter<ViewItem, ViewBinding>() {

    private val list: List<ViewItemAdapter<ViewItem, ViewBinding>> by lazy {

        val adapters = arrayListOf<ViewItemAdapter<ViewItem, ViewBinding>>()

        adapters.addAll(adapter)

        val listViewItemClass by lazy {
            adapter.map { it.viewItemClass }
        }

        val listAdapterFromProvider = if (adapter.isEmpty()) {
            providerItemAdapter()
        } else providerItemAdapter().filter {
            it.viewItemClass !in listViewItemClass
        }
        adapters.addAll(listAdapterFromProvider)

        adapters
    }

    private val typeAndAdapter: Map<Int, ViewItemAdapter<ViewItem, ViewBinding>> by lazy {

        val map = HashMap<Int, ViewItemAdapter<ViewItem, ViewBinding>>()

        list.forEachIndexed { index, viewItemAdapter ->
            map[index] = viewItemAdapter
        }

        map
    }

    private val viewItemClassAndType: Map<Class<*>, Int> by lazy {

        val map = HashMap<Class<*>, Int>()

        list.forEachIndexed { index, viewItemAdapter ->
            map[viewItemAdapter.viewItemClass] = index
        }

        map
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        list.forEach {
            it.adapter = this
            it.onAttachedToRecyclerView(recyclerView)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        list.forEach {
            it.adapter = null
            it.onDetachedFromRecyclerView(recyclerView)
        }
    }

    override fun getItemViewType(position: Int): Int {

        val itemClass = getItem(position).javaClass

        return viewItemClassAndType[itemClass] ?: error("not found adapter support item ${itemClass.name}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ViewBinding> {

        return typeAndAdapter[viewType]!!.createViewHolder(parent, viewType) ?: super.onCreateViewHolder(parent, viewType)
    }

    override fun createViewBinding(parent: ViewGroup, viewType: Int): ViewBinding {

        return typeAndAdapter[viewType]!!.createViewBinding(parent, viewType)
    }

    override fun onViewAttachedToWindow(holder: BaseBindingViewHolder<ViewBinding>) {

        typeAndAdapter[holder.viewType]?.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: BaseBindingViewHolder<ViewBinding>) {

        typeAndAdapter[holder.viewType]?.onViewDetachedFromWindow(holder)
    }

    override fun bind(binding: ViewBinding, viewType: Int, position: Int, item: ViewItem, payloads: MutableList<Any>) {

        typeAndAdapter[viewType]?.onBindViewHolder(binding, viewType, position, item, payloads)
    }

    override fun bind(binding: ViewBinding, viewType: Int, position: Int, item: ViewItem) {

        typeAndAdapter[viewType]?.onBindViewHolder(binding, viewType, position, item)
    }

    private fun providerItemAdapter(): List<ViewItemAdapter<ViewItem, ViewBinding>> {

        return provider.flatMap { it.provider() }.filterIsInstance<ViewItemAdapter<ViewItem, ViewBinding>>()
    }
}