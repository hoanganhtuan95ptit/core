package com.simple.coreapp.utils.ext

import android.view.View
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