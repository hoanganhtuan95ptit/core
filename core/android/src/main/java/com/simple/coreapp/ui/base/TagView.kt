package com.simple.coreapp.ui.base

import androidx.annotation.MainThread
import java.io.Closeable
import java.io.IOException

interface TagView {

    var mCleared: Boolean

    val mBagOfTags: MutableMap<String, Any>


    @MainThread
    fun tagClear() {

        mCleared = true

        synchronized(mBagOfTags) {

            for (value in mBagOfTags.values) {
                closeWithRuntimeException(value)
            }

            mBagOfTags.clear()
        }
    }

    fun <T : Any> setTagIfAbsent(key: String, newValue: T): T {

        var previous: T?

        synchronized(mBagOfTags) {

            previous = mBagOfTags[key] as T?

            if (previous == null) {
                mBagOfTags[key] = newValue
            }
        }

        val result = if (previous == null) newValue else previous!!

        if (mCleared) {
            closeWithRuntimeException(result)
        }

        return result
    }

    fun <T> getTag(key: String): T? {

        synchronized(mBagOfTags) {
            return mBagOfTags[key] as T?
        }
    }

    private fun closeWithRuntimeException(obj: Any) {

        if (obj is Closeable) try {
            obj.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}