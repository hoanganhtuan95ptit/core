package com.one.core.utils.extentions


fun <T> Collection<T>.toArrayList(): ArrayList<T> {
    return ArrayList(this)
}

inline fun <T, L : Collection<T>> L.validateIndex(action: L.(index: Int, T) -> Unit): L {
    forEachIndexed { i, t ->
        action(i, t)
    }
    return this
}

inline fun <T, L : Collection<T>> L.validate(validate: T.() -> Unit): L {
    forEachIndexed { _, t ->
        validate(t)
    }
    return this
}
