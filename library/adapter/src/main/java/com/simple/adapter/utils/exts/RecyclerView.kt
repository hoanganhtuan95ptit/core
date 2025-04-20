package com.simple.adapter.utils.exts

import android.transition.ChangeBounds
import android.transition.Fade
import android.transition.Scene
import android.transition.Transition
import android.transition.TransitionManager
import android.transition.TransitionSet
import androidx.recyclerview.widget.RecyclerView
import com.simple.adapter.MultiAdapter
import com.simple.adapter.entities.ViewItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

suspend fun RecyclerView.submitListAwait(viewItemList: List<ViewItem>) = channelFlow {

    val adapter = adapter as? MultiAdapter

    if (adapter == null) {

        trySend(false)
        awaitClose()
        return@channelFlow
    }

    adapter.submitList(viewItemList) {

        trySend(true)
    }

    awaitClose {

    }
}.first()

suspend fun RecyclerView.submitListAwait(viewItemList: List<ViewItem>, isTransitionAwait: Boolean = false) {

    submitListAwait(viewItemList = viewItemList)

    if (isTransitionAwait) {

        transitionAwait()
    }
}

suspend fun RecyclerView.transitionAwait(transition: Transition = TransitionSet().addTransition(ChangeBounds().setDuration(350)).addTransition(Fade().setDuration(350))) = channelFlow<Unit> {

    val timeoutJob = launch {

        delay(100)
        trySend(Unit)
    }

    val transitionListener = object : Transition.TransitionListener {

        override fun onTransitionStart(transition: Transition) {
            timeoutJob.cancel()
        }

        override fun onTransitionEnd(transition: Transition) {
            trySend(Unit)
        }

        override fun onTransitionCancel(transition: Transition) {
            trySend(Unit)
        }

        override fun onTransitionPause(transition: Transition) {
        }

        override fun onTransitionResume(transition: Transition) {
        }
    }

    TransitionManager.go(Scene(this@transitionAwait), transition.addListener(transitionListener))

    awaitClose {
        transition.removeListener(transitionListener)
    }
}.first()