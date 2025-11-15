package com.simple.coreapp.utils.ext

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowInsets
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.annotation.Px
import androidx.core.animation.doOnEnd
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.LifecycleOwner
import androidx.transition.AutoTransition
import androidx.transition.Scene
import androidx.transition.Transition
import androidx.transition.Transition.TransitionListener
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.simple.coreapp.utils.extentions.animation
import com.unknown.coroutines.launchCollect
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine


fun View.left(parentIdStop: Int? = null): Int {

    return if (id == parentIdStop) {

        0
    } else if (parent !is View || parentIdStop == null) {

        left
    } else {

        (parent as View).left(parentIdStop) + left
    }
}

fun View.right(parentIdStop: Int? = null): Int {

    return left(parentIdStop) + width
}

fun View.leftCenter(parentIdStop: Int? = null): Int {

    return left(parentIdStop) + width / 2
}

fun View.top(parentIdStop: Int? = null): Int {

    return if (id == parentIdStop) {

        0
    } else if (parent !is View || parentIdStop == null) {

        top
    } else {

        (parent as View).top(parentIdStop) + top
    }
}

fun View.bottom(parentIdStop: Int? = null): Int {

    return top(parentIdStop) + height
}

fun View.center(parentIdStop: Int? = null): Int {

    return top(parentIdStop) + height / 2
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.setGone(gone: Boolean) {
    this.visibility = if (gone) View.GONE else View.VISIBLE
}

fun View.setVisible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.setInvisible(isInvisible: Boolean) {
    this.visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
}

fun View.isGone() = visibility == View.GONE

fun View.isVisible() = visibility == View.VISIBLE

fun View.isInVisible() = visibility == View.INVISIBLE

fun View.resize(width: Int = -3, height: Int = -3, weight: Float = 0f): Boolean {

    var update = false

    val params = layoutParams as ViewGroup.LayoutParams

    if (params.width != width && width >= -2) {
        params.width = width
        update = true
    }

    if (params.height != height && height >= -2) {
        params.height = height
        update = true
    }

    if (params is LinearLayout.LayoutParams && params.weight != weight) {
        params.weight = weight
        update = true
    }

    if (update) {
        layoutParams = layoutParams
    }

    return update
}

inline fun View.updateMargin(@Px left: Int = marginLeft, @Px top: Int = marginTop, @Px right: Int = marginRight, @Px bottom: Int = marginBottom) {

    var update = false
    val layoutParams = layoutParams as? ViewGroup.MarginLayoutParams ?: return

    if (layoutParams.leftMargin != left) {
        update = true
        layoutParams.leftMargin = left
    }

    if (layoutParams.topMargin != top) {
        update = true
        layoutParams.topMargin = top
    }

    if (layoutParams.rightMargin != right) {
        update = true
        layoutParams.rightMargin = right
    }

    if (layoutParams.bottomMargin != bottom) {
        update = true
        layoutParams.bottomMargin = bottom
    }

    if (update) {
        this.layoutParams = layoutParams
    }
}

fun View.resizeAnim(width: Int = -3, height: Int = -3, weight: Float = 0f, duration: Long = 350, onEnd: () -> Unit = {}) {

    val list = arrayListOf<PropertyValuesHolder>()

    val params = layoutParams as ViewGroup.LayoutParams

    if (params.width != width && width >= -2) {
        list.add(PropertyValuesHolder.ofInt("width", params.width, width))
    }

    if (params.height != height && height >= -2) {
        list.add(PropertyValuesHolder.ofInt("height", params.height, height))
    }

    if (params is LinearLayout.LayoutParams && params.weight != weight) {
        list.add(PropertyValuesHolder.ofFloat("weight", params.weight, weight))
    }

    if (list.isNotEmpty()) {

        val anim = list.animation(onUpdate = {
            resize(
                it.getAnimatedValue("width") as? Int ?: params.width,
                it.getAnimatedValue("height") as? Int ?: params.height,
                it.getAnimatedValue("weight") as? Float ?: (params as? LinearLayout.LayoutParams)?.weight ?: 0f
            )
        }, duration = duration, onEnd = onEnd)

        tag = anim
    } else {

        tag = null
        onEnd.invoke()
    }
}


suspend fun View.awaitResizeAnim(width: Int = -3, height: Int = -3, weight: Float = 0f, duration: Long = 350) = channelFlow {

    val list = arrayListOf<PropertyValuesHolder>()

    val params = layoutParams as ViewGroup.LayoutParams

    if (params.width != width && width >= -2) {
        list.add(PropertyValuesHolder.ofInt("width", params.width, width))
    }

    if (params.height != height && height >= -2) {
        list.add(PropertyValuesHolder.ofInt("height", params.height, height))
    }

    if (params is LinearLayout.LayoutParams && params.weight != weight) {
        list.add(PropertyValuesHolder.ofFloat("weight", params.weight, weight))
    }

    if (list.isEmpty()) {

        trySend(Unit)
        return@channelFlow
    }

    val anim = list.animation(onUpdate = {
        resize(
            it.getAnimatedValue("width") as? Int ?: params.width,
            it.getAnimatedValue("height") as? Int ?: params.height,
            it.getAnimatedValue("weight") as? Float ?: (params as? LinearLayout.LayoutParams)?.weight ?: 0f
        )
    }, duration = duration, onEnd = {

        trySend(Unit)
    })

    awaitClose {

        anim.cancel()
    }
}.firstOrNull()


fun View.setPaddingTop(top: Int = 0) {

    if (paddingTop != top) {
        setPadding(paddingLeft, top, paddingRight, paddingBottom)
    }
}

fun View.getHeightVisible(): Int {
    val r = getLocalVisibleRect()
    return r.bottom - r.top
}

fun View.getLocationOnScreen(): IntArray {
    val location = IntArray(2)
    getLocationOnScreen(location)
    return location
}

fun View.getLocalVisibleRect(): Rect {
    val r = Rect()
    getLocalVisibleRect(r)
    return r
}

fun View.setMarginTop(top: Int) {
    (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
        this.topMargin = top
    }?.let {
        layoutParams = it
    }
}

fun View.setMarginBottom(bottom: Int) {

    val params = (layoutParams as? ViewGroup.MarginLayoutParams) ?: return

    if (params.bottomMargin == bottom) {
        return
    }

    params.bottomMargin = bottom

    layoutParams = params
}

fun View.getString(res: Int, vararg formatArgs: Any) = context.getString(res, *formatArgs)

fun View.showKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.showSoftInput(this.findFocus(), InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(this.findFocus().windowToken, 0)
}

fun View.setDebouncedClickListener(
    debounceIntervalMs: Int = 1000,
    listener: (view: View) -> Unit
) {
    var lastTapTimestamp: Long = 0
    val customListener = View.OnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTapTimestamp > debounceIntervalMs) {
            lastTapTimestamp = currentTime
            listener.invoke(it)
        }
    }
    this.setOnClickListener(customListener)
}

fun View.alphaAnim(alpha: Float, duration: Long = 350, onEnd: () -> Unit = {}) {
    animate().cancel()

    animate().apply {
        this.withEndAction(onEnd)

        this.interpolator = LinearInterpolator()
        this.duration = duration
        this.alpha(alpha)
        this.start()
    }
}

fun View.scaleAnim(to: Float, delay: Long = 0, duration: Long = 350, onStart: () -> Unit = {}, onEnd: () -> Unit = {}) {
    animate().cancel()

    if (duration <= 0) {
        onStart.invoke()
        scaleX = to
        scaleY = to
        onEnd.invoke()
    } else {
        animate().scaleX(to).scaleY(to).setStartDelay(delay).setDuration(duration).withStartAction(onStart).withEndAction(onEnd).start()
    }
}


fun View.translationAnim(toY: Float, duration: Long = 350, onEnd: () -> Unit = {}) {
    animate().apply {
        this.withEndAction(onEnd)

        this.interpolator = LinearInterpolator()
        this.duration = duration
        this.translationY(toY)
        this.start()
    }
}

@OptIn(InternalCoroutinesApi::class)
suspend fun View.awaitAnim() = suspendCancellableCoroutine<Boolean> { continuation ->

    if ((tag as? ValueAnimator) == null || !(tag as ValueAnimator).isRunning) {

        if (!continuation.isCompleted) continuation.tryResume(false)
        return@suspendCancellableCoroutine
    }

    (tag as ValueAnimator).doOnEnd {

        if (!continuation.isCompleted) continuation.tryResume(true)
    }

    animate().withEndAction {

        if (!continuation.isCompleted) continuation.tryResume(true)
    }
}

suspend fun View.awaitPost() = channelFlow {

    post {

        trySend(Unit)
    }

    awaitClose {

    }
}.firstOrNull()

fun ViewGroup.show(startView: View, endView: View) {

    val transform = MaterialContainerTransform().apply {

        duration = 350

        this.startView = startView
        this.endView = endView

        addTarget(endView)
        setPathMotion(MaterialArcMotion())

        interpolator = FastOutSlowInInterpolator()
        scrimColor = Color.TRANSPARENT
    }

    TransitionManager.beginDelayedTransition(this, transform)

    startView.visibility = View.GONE
    endView.visibility = View.VISIBLE
}

suspend fun View.awaitDraw(timeout: Long = 350) = channelFlow {

    var job: Job? = null

    val runnable = kotlinx.coroutines.Runnable {

        job?.cancel()
        job = launch {

            delay(timeout)
            trySend(Unit)
        }
    }

    val listener = ViewTreeObserver.OnDrawListener {

        runnable.run()
    }

    post {

        runnable.run()
    }

    viewTreeObserver.addOnDrawListener(listener)

    awaitClose {

        viewTreeObserver.removeOnDrawListener(listener)
    }
}.first()

suspend fun View.awaitAmin(vararg target: View, transition: TransitionSet = AutoTransition().setDuration(350)) = channelFlow {

    val timeoutJob = launch {

        delay(350)
        trySend(Unit)
    }

    anim(*target, transition = transition, listener = object : TransitionListener {

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
    })

    awaitClose()
}.first()

fun View.anim(vararg target: View, transition: TransitionSet = AutoTransition().setDuration(350), listener: TransitionListener? = null) {

    if (this !is ViewGroup) {

        listener?.onTransitionCancel(transition)
        return
    }

    target.forEach {

        transition.addTarget(it)
    }

    listener?.let {

        transition.addListener(it)
    }

    TransitionManager.go(Scene(this), transition)
}


fun View.doOnChangeHeightStatusAndHeightNavigation(
    lifecycleOwner: LifecycleOwner, onChange: suspend (heightStatusBar: Int, heightNavigationBar: Int) -> Unit
) = listenerOnChangeHeightStatusAndHeightNavigation().distinctUntilChanged().launchCollect(lifecycleOwner) {

    onChange(it.first, it.second)
}

fun View.listenerOnChangeHeightStatusAndHeightNavigation() = listenerOnApplyWindowInsetsAsync().mapNotNull { insets ->

    val heightStatusBar = insets.getStatusBar().takeIf { it >= 10 } ?: getStatusBarHeight(context)
    val heightNavigationBar = insets.getNavigationBar().takeIf { it >= 10 } ?: getStatusBarHeight(context)

    if (heightStatusBar <= 0 || heightNavigationBar <= 0) {
        null
    } else {
        Pair(heightStatusBar, heightNavigationBar)
    }
}

private fun View.listenerOnApplyWindowInsetsAsync() = channelFlow {

    val listener = View.OnApplyWindowInsetsListener { _, insets ->

        trySend(insets)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsets.CONSUMED
        } else {
            insets
        }
    }

    setOnApplyWindowInsetsListener(listener)

    rootWindowInsets?.let {

        trySend(it)
    }

    post {

        trySend(rootWindowInsets ?: return@post)
    }

    awaitClose {

        setOnApplyWindowInsetsListener(null)
    }
}
