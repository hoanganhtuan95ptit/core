package com.simple.coreapp.utils.extentions

import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentContainerView
import androidx.transition.Scene
import androidx.transition.Transition
import androidx.transition.TransitionManager
import kotlinx.coroutines.suspendCancellableCoroutine

fun ViewGroup.inflate(@LayoutRes l: Int, theme: Int = -1): View {

    val contextThemeWrapper = if (theme > 0) {
        ContextThemeWrapper(context, theme)
    } else {
        context
    }

    return LayoutInflater.from(context).cloneInContext(contextThemeWrapper).inflate(l, this, false)
}

fun ViewGroup.remove(view: View) {
    removeView(view)
}

fun ViewGroup.add(view: View) = (view.parent as? ViewGroup)?.takeIf {
    it != this
}?.let {
    it.remove(view)
    addView(view, childCount)
}

fun ViewGroup.findFragmentContainerView(): FragmentContainerView? {

    for (i in 0..childCount) {
        val child = getChildAt(i)

        if (child is FragmentContainerView) {
            return child
        } else if (child is ViewGroup) {
            return child.findFragmentContainerView()
        }
    }

    return null
}

suspend fun ViewGroup.beginTransitionAwait(transition: Transition, action: () -> Unit = {}) = suspendCancellableCoroutine { continuation ->

    val transitionListener = object : Transition.TransitionListener {

        override fun onTransitionStart(transition: Transition) {
        }

        override fun onTransitionEnd(transition: Transition) {

            continuation.resumeActive(true)
        }

        override fun onTransitionCancel(transition: Transition) {

            continuation.resumeActive(true)
        }

        override fun onTransitionPause(transition: Transition) {
        }

        override fun onTransitionResume(transition: Transition) {
        }
    }

    TransitionManager.go(Scene(this), transition.addListener(transitionListener))

    action()

    continuation.invokeOnCancellation { transition.removeListener(transitionListener) }
}