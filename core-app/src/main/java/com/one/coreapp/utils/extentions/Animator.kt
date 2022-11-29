package com.one.coreapp.utils.extentions

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import androidx.core.animation.doOnEnd

fun List<PropertyValuesHolder>.animation(startDelay: Long = 0, duration: Long = 350, onStart: () -> Unit = {}, onUpdate: (ValueAnimator) -> Unit = { _ -> }, onEnd: () -> Unit = {}): ValueAnimator {

    onStart.invoke()

    val animator = ValueAnimator.ofPropertyValuesHolder(*this.toTypedArray())

    animator.addUpdateListener {
        onUpdate.invoke(it)
    }

    animator.doOnEnd {
        onEnd.invoke()
    }

    animator.duration = duration
    animator.startDelay = startDelay
    animator.start()

    return animator
}

class ValuesHolder(val key: String, val from: Any, val to: Any) {

    fun to(): PropertyValuesHolder {
        return if (from is Int && to is Int) {
            PropertyValuesHolder.ofInt(key, from, to)
        } else {
            PropertyValuesHolder.ofFloat(key, (from as Float), (to as Float))
        }
    }
}
