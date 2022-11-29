@file:Suppress("UNCHECKED_CAST", "LocalVariableName")

package com.one.coreapp.ui.base.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.*
import androidx.viewbinding.ViewBinding
import com.one.coreapp.App
import com.one.coreapp.TRANSITION
import com.one.coreapp.databinding.ItemLoadMoreBinding
import com.one.coreapp.databinding.ItemLoadingBinding
import com.one.coreapp.utils.extentions.findGenericClassBySuperClass
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume

abstract class BaseAsyncAdapter<T : ViewItem, B : ViewBinding>(
    private val onLoadMore: () -> Unit = {},
    private val itemCallback: DiffUtil.ItemCallback<ViewItem>? = null,
) : ListAdapter<ViewItem, BaseBindingViewHolder<ViewBinding>>(
    AsyncDifferConfig
        .Builder(itemCallback ?: DefaultItemCallback<ViewItem>())
        .setBackgroundThreadExecutor(App.shared.dispatcherForHandleDataUi.executor).build()
) {


    private var topAdapterDataObserver = TopAdapterDataObserver()


    open var itemClass: Class<ViewItem>? = findGenericClassBySuperClass(ViewItem::class.java)?.java

    open var bindingClass: Class<ViewBinding>? = findGenericClassBySuperClass(ViewBinding::class.java)?.java


    fun scrollTop(scrollTop: Boolean) {
        topAdapterDataObserver.canScrollTop = scrollTop
    }

    suspend fun submitListSync(@Nullable list: List<T>) = suspendCancellableCoroutine<Boolean> {

        submitList(list) {
            if (!it.isCompleted) it.resume(true)
        }
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


    override fun getItemViewType(position: Int): Int = when (val item = getItem(position)) {
        is LoadMoreViewItem -> TYPE_LOAD_MORE
        is LoadingViewItem -> item.data
        else -> 0
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<ViewBinding> {

        val view = if (viewType > 100) {

            LayoutInflater.from(parent.context)?.inflate(viewType, parent, false)
        } else {

            null
        }

        val binding = if (viewType == TYPE_LOAD_MORE) {

            createLoadMoreView(parent)
        } else if (view != null) {

            createLoadingView(parent, view)
        } else {

            createView(parent, viewType)
        }

        return BaseBindingViewHolder(binding, viewType)
    }

    protected abstract fun createView(parent: ViewGroup, viewType: Int? = 0): B

    protected open fun createLoadMoreView(parent: ViewGroup): ItemLoadMoreBinding {

        val binding = ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)


        return binding
    }

    protected open fun createLoadingView(parent: ViewGroup, view: View): ItemLoadingBinding {

        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.root.addView(view)

        return binding
    }


    override fun onViewAttachedToWindow(holder: BaseBindingViewHolder<ViewBinding>) {
        super.onViewAttachedToWindow(holder)

        if (holder.binding is ItemLoadMoreBinding) {
            onLoadMore.invoke()
        }

        (holder.binding as? ItemLoadingBinding)?.root?.startShimmerAnimation()
    }

    override fun onViewDetachedFromWindow(holder: BaseBindingViewHolder<ViewBinding>) {
        super.onViewDetachedFromWindow(holder)

        (holder.binding as? ItemLoadingBinding)?.root?.stopShimmerAnimation()
    }


    override fun onBindViewHolder(holder: BaseBindingViewHolder<ViewBinding>, position: Int, payloads: MutableList<Any>) {

        holder.binding.root.tag = position

        val item = getItem(position)
        val binding = holder.binding

        val _item = item.takeIf { itemClass?.isInstance(it) == true }
        val _binding = binding.takeIf { bindingClass?.isInstance(it) == true }

        val payload = payloads.getOrNull(0) as? MutableList<Any>


        if (!payload.isNullOrEmpty() && _binding != null && _item != null) {

            bind(_binding as B, holder.viewType, position, _item as T, payload)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    protected open fun bind(binding: @UnsafeVariance B, viewType: Int, position: Int, item: T, payloads: MutableList<Any>) {
    }


    override fun onBindViewHolder(holder: BaseBindingViewHolder<ViewBinding>, position: Int) {

        holder.binding.root.tag = position
        holder.binding.root.transitionName = "$TRANSITION$position"

        val item = getItem(position)
        val binding = holder.binding

        val _item = item.takeIf { itemClass?.isInstance(it) == true }
        val _binding = binding.takeIf { bindingClass?.isInstance(it) == true }



        if (item is LoadMoreViewItem) {

            bindLoadMore(binding as ItemLoadMoreBinding, holder.viewType, position, item)
        } else if (_binding != null && _item != null) {

            bind(_binding as B, holder.viewType, position, _item as T)
        }
    }

    protected open fun bindLoadMore(binding: ItemLoadMoreBinding, viewType: Int, position: Int, item: LoadMoreViewItem) {

        val layoutParams = binding.root.layoutParams as? StaggeredGridLayoutManager.LayoutParams ?: return
        layoutParams.isFullSpan = true
    }

    protected open fun bind(binding: @UnsafeVariance B, viewType: Int, position: Int, item: T) {
    }


    fun <T> getViewItem(binding: ViewBinding): T? {

        return (binding.root.tag as? Int)?.let { this.getItem(it) as? T }
    }

    companion object {

        const val TYPE_LOAD_MORE = Int.MIN_VALUE
    }
}


class BaseBindingViewHolder<B : ViewBinding>(val binding: B, val viewType: Int) : RecyclerView.ViewHolder(binding.root)


class DefaultItemCallback<T : ViewItem> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.areItemsTheSame() == newItem.areItemsTheSame()
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return false
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


interface ViewItem {

    fun areItemsTheSame(): List<Any>

    fun getContentsCompare(): List<Pair<Any, String>> = listOf()
}

interface ViewItemCloneable : ViewItem {

    fun clone() = this
}

data class LoadMoreViewItem(
    val id: String = UUID.randomUUID().toString()
) : ViewItemCloneable {

    override fun clone() = copy()

    override fun areItemsTheSame(): List<Any> = listOf(
        javaClass.simpleName, id
    )
}

data class LoadingViewItem(val data: Int) : ViewItemCloneable {

    override fun areItemsTheSame(): List<Any> = listOf("LoadingViewItem", data)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LoadingViewItem) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data
    }


}

