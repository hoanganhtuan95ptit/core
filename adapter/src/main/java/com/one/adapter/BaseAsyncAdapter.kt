package com.one.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.one.core.entities.Comparable
import kotlinx.coroutines.newSingleThreadContext
import java.io.Serializable


private val dispatcherForHandleDataUi = newSingleThreadContext("handle_data_for_ui")


abstract class BaseAsyncAdapter<T : ViewItemCloneable, B : ViewBinding>(
    itemCallback: DiffUtil.ItemCallback<T>? = null,
) : ListAdapter<T, BaseBindingViewHolder<B>>(
    AsyncDifferConfig
        .Builder(itemCallback ?: DefaultItemCallback<T>())
        .setBackgroundThreadExecutor(dispatcherForHandleDataUi.executor).build()
) {


    private var topAdapterDataObserver = TopAdapterDataObserver()


    fun scrollTop(scrollTop: Boolean) {

        topAdapterDataObserver.canScrollTop = scrollTop
    }

    open fun setRecyclerView(recyclerView: RecyclerView) {

        recyclerView.adapter = this
        recyclerView.layoutManager = getLayoutManager(recyclerView.context)
    }

    open fun getLayoutManager(context: Context): RecyclerView.LayoutManager {

        return LinearLayoutManager(context)
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        topAdapterDataObserver.recyclerView = recyclerView
        registerAdapterDataObserver(topAdapterDataObserver)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        topAdapterDataObserver.recyclerView = null
        unregisterAdapterDataObserver(topAdapterDataObserver)
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


class DefaultItemCallback<T : ViewItem> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {

        return oldItem.areItemsTheSame() == newItem.areItemsTheSame()
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

@Keep
interface ViewItem : Serializable, Comparable {

    fun areItemsTheSame(): List<Any>

    fun getContentsCompare(): List<Pair<Any, String>> = listOf()

    override fun getListCompare(): List<*> = listOf(*areItemsTheSame().toTypedArray(), *getContentsCompare().map { it.first }.toTypedArray())
}

@Keep
interface ViewItemCloneable : ViewItem {

    fun clone() = this
}
