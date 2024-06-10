package com.simple.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.simple.adapter.entities.ViewItem

class MultiAdapter(

    vararg adapter: ViewItemAdapter<ViewItem, ViewBinding>,

    private val onLoadMore: (() -> Unit)? = null,

    private val onViewHolderAttachedToWindow: ((BaseBindingViewHolder<*>) -> Unit)? = null,
    private val onViewHolderDetachedFromWindow: ((BaseBindingViewHolder<*>) -> Unit)? = null,
) : BaseAsyncAdapter<ViewItem, ViewBinding>() {

    val list: List<ViewItemAdapter<ViewItem, ViewBinding>> by lazy {

        val adapters = arrayListOf<ViewItemAdapter<ViewItem, ViewBinding>>()

        adapters.add(SpaceAdapter())
        adapters.add(LoadingViewAdapter())
        adapters.add(LoadMoreViewAdapter(onLoadMore))

        adapters.addAll(adapter)

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

        return viewItemClassAndType[getItem(position).javaClass] ?: super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ViewBinding> {

        return typeAndAdapter[viewType]!!.createViewHolder(parent, viewType) ?: super.onCreateViewHolder(parent, viewType)
    }

    override fun createView(parent: ViewGroup, viewType: Int): ViewBinding {

        return typeAndAdapter[viewType]!!.createViewItem(parent, viewType)
    }


    override fun onViewAttachedToWindow(holder: BaseBindingViewHolder<ViewBinding>) {

        typeAndAdapter[holder.viewType]?.onViewAttachedToWindow(holder)

        onViewHolderAttachedToWindow?.invoke(holder)
    }

    override fun onViewDetachedFromWindow(holder: BaseBindingViewHolder<ViewBinding>) {

        typeAndAdapter[holder.viewType]?.onViewDetachedFromWindow(holder)

        onViewHolderDetachedFromWindow?.invoke(holder)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<ViewBinding>, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)

        typeAndAdapter[holder.viewType]?.onBindViewHolder(holder, holder.binding, position)
    }

    override fun bind(binding: ViewBinding, viewType: Int, position: Int, item: ViewItem, payloads: MutableList<Any>) {

        typeAndAdapter[viewType]?.bindView(binding, viewType, position, item, payloads)
    }

    override fun bind(binding: ViewBinding, viewType: Int, position: Int, item: ViewItem) {

        typeAndAdapter[viewType]?.bindView(binding, viewType, position, item)
    }
}