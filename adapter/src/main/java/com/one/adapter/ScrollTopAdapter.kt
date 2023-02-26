package com.one.adapter

import androidx.recyclerview.widget.RecyclerView

class TopAdapterDataObserver : RecyclerView.AdapterDataObserver() {

    var canScrollTop: Boolean = false

    var recyclerView: RecyclerView? = null

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        scrollToTop()
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        scrollToTop()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        scrollToTop()
    }

    private fun scrollToTop() {
        if (canScrollTop) recyclerView?.scrollToPosition(0)
    }
}
