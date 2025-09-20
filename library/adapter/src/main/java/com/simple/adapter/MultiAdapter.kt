package com.simple.adapter

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.simple.autobind.AutoBind
import com.simple.autobind.utils.exts.createObject
import com.simple.adapter.base.BaseAsyncAdapter
import com.simple.adapter.base.BaseBindingViewHolder
import com.simple.adapter.entities.ViewItem
import com.simple.adapter.provider.AdapterProvider

class MultiAdapter(
    vararg adapter: ViewItemAdapter<ViewItem, ViewBinding>,
) : BaseAsyncAdapter<ViewItem, ViewBinding>() {


    private val adapters = ArrayList<ViewItemAdapter<ViewItem, ViewBinding>>()

    private val adapterClassNames = ArrayList<String>()


    private val typeAndAdapter = HashMap<Int, ViewItemAdapter<ViewItem, ViewBinding>>()

    private val viewItemClassAndType = HashMap<Class<*>, Int>()


    init {

        updateViewItemAdapter(adapter.toList())
        refreshViewItemAdapter()
    }

    override fun getItemViewType(position: Int): Int {

        return getItemViewTypeOrDefault(position)
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

    private fun getItemViewTypeOrDefault(position: Int): Int {

        val itemClass = getItem(position).javaClass

        return viewItemClassAndType[itemClass] ?: refreshViewItemAdapter().let {

            viewItemClassAndType[itemClass] ?: error("not found adapter support item ${itemClass.name}")
        }
    }

    private fun refreshViewItemAdapter() {

        val adapterProviderClass = AdapterProvider::class.java

        val adapterClassNameNew = AutoBind.loadName(adapterProviderClass).filter { it !in adapterClassNames }
        if (adapterClassNameNew.isEmpty()) return

        adapterClassNames.addAll(adapterClassNameNew)

        val viewItemAdapter = adapterClassNameNew.flatMap { it.createObject(adapterProviderClass)?.provider().orEmpty() }.filterIsInstance<ViewItemAdapter<*, *>>()
        updateViewItemAdapter(viewItemAdapter)
    }

    private fun updateViewItemAdapter(viewItemAdapter: List<ViewItemAdapter<*, *>>) {

        if (viewItemAdapter.isEmpty()) return

        val viewItemAdapter = viewItemAdapter.filter { viewItemClassAndType[it.viewItemClass] == null }
        viewItemAdapter.map { it.adapter = this }
        adapters.addAll(viewItemAdapter)

        val size = adapters.size

        viewItemAdapter.forEachIndexed { index, viewItemAdapter ->

            val type = size + index

            typeAndAdapter.put(type, viewItemAdapter)
            viewItemClassAndType.put(viewItemAdapter.viewItemClass, type)
        }
    }
}