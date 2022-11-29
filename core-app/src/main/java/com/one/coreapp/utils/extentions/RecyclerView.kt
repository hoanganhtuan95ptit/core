@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package com.one.coreapp.utils.extentions

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

inline fun <reified A : RecyclerView.Adapter<*>> RecyclerView.getAdapter(): A? = adapter as? A

@Suppress("CAST_NEVER_SUCCEEDS", "NAME_SHADOWING")
fun RecyclerView.getViewCenter(size: Int? = null): View? = getViewBy(getPositionCenter(size))

fun RecyclerView.getViewVisible(): List<View> {
    val manager = layoutManager as? LinearLayoutManager ?: error("only support LinearLayoutManager")

    val first = manager.findFirstCompletelyVisibleItemPosition()
    val last = manager.findLastCompletelyVisibleItemPosition()

    val list = arrayListOf<View>()

    for (i in first..last) {
        getViewBy(i)?.let { list.add(it) }
    }

    return list
}

@Suppress("CAST_NEVER_SUCCEEDS", "NAME_SHADOWING")
fun RecyclerView.getPositionCenter(size: Int? = null): Int {
    val manager = layoutManager as? LinearLayoutManager ?: error("only support LinearLayoutManager")

    val first = manager.findFirstCompletelyVisibleItemPosition()
    val last = manager.findLastCompletelyVisibleItemPosition()

    val size = size ?: adapter?.itemCount ?: 0

    return if (last - first == 1 && first == 0 && last == size - 1 && isVisible(getViewBy(first))) {
        first
    } else if (last - first == 1 && first == 0 && last == size - 1) {
        last
    } else if (last - first == 1 && first == 0) {
        first
    } else if (last - first == 1 && last == size - 1) {
        size - 1
    } else {
        (first + last) / 2
    }
}

fun isVisible(view: View?): Boolean {
    if (view == null) {
        return false
    }
    if (!view.isShown) {
        return false
    }

    val parent = view.parent as? ViewGroup ?: return false

    val point = IntArray(2)
    view.getLocationOnScreen(point)

    return point[0] + view.width / 2 in (parent.width / 2 - 100)..(parent.width / 2 + 100)

}

suspend fun RecyclerView.smoothScrollToSync(position: Int) = suspendCancellableCoroutine<Boolean> { cancellableContinuation ->

    smoothScrollTo(position) {
        if (!cancellableContinuation.isCompleted) cancellableContinuation.resume(true)
    }
}

fun RecyclerView.smoothScrollTo(position: Int, onCompleted: () -> Unit = {}) {

    val childView = getViewBy(position)

    if (position < 0 || childView?.top == scrollY) {

        onCompleted.invoke()
        return
    }

    object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            if (newState != RecyclerView.SCROLL_STATE_IDLE) return

            removeOnScrollListener(this)

            if (childView != null) {

                onCompleted.invoke()
            } else {
                smoothScrollTo(position, onCompleted)
            }
        }
    }.apply {

        addOnScrollListener(this)
    }

    if (childView != null) {

        smoothScrollTo(childView)
    } else {

        scrollToPosition(position)
    }
}

fun RecyclerView.smoothScrollTo(view: View, onCompleted: () -> Unit = {}) {
    val runnable = Runnable {
        onCompleted.invoke()
    }

    val onScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            removeCallbacks(runnable)
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                removeOnScrollListener(this)
                runnable.run()
            }
        }
    }

    addOnScrollListener(onScrollListener)

    postDelayed(runnable, 200)

    smoothScrollTo(view)
}

fun RecyclerView.smoothScrollTo(view: View) = view.post {

    var distance = view.top
    var viewParent = view.parent

    for (i in 0..9) {
        if ((viewParent as View) === this) break

        distance += (viewParent as View).top

        viewParent = viewParent.getParent()
    }

    smoothScrollBy(0, distance)
}


fun RecyclerView.getViewBy(position: Int): View? {
    println("getViewBy $position")
    return findViewHolderForAdapterPosition(position)?.itemView
}