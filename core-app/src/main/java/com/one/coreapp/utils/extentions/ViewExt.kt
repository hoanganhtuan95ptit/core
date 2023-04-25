package com.one.coreapp.utils.extentions

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
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
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


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

suspend fun View.awaitAnim() = suspendCancellableCoroutine<Boolean> { continuation ->

    if ((tag as? ValueAnimator) == null || !(tag as ValueAnimator).isRunning) {

        if (!continuation.isCompleted) continuation.resume(false)
        return@suspendCancellableCoroutine
    }

    (tag as ValueAnimator).doOnEnd {

        if (!continuation.isCompleted) continuation.resume(true)
    }

    animate().withEndAction {

        if (!continuation.isCompleted) continuation.resume(true)
    }
}

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