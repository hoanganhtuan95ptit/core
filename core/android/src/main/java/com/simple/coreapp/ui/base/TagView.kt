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
        // Since clear() is final, this method is still called on mock objects
        // and in those cases, mBagOfTags is null. It'll always be empty though
        // because setTagIfAbsent and getTag are not final so we can skip
        // clearing it
        synchronized(mBagOfTags) {
            for (value in mBagOfTags.values) {
                // see comment for the similar call in setTagIfAbsent
                closeWithRuntimeException(value)
            }
        }
    }

    fun <T : Any> setTagIfAbsent(key: String, newValue: T): T {
        var previous: T?
        synchronized(mBagOfTags!!) {
            previous = mBagOfTags[key] as T?
            if (previous == null) {
                mBagOfTags[key] = newValue
            }
        }
        val result = if (previous == null) newValue else previous!!
        if (mCleared) {
            // It is possible that we'll call close() multiple times on the same object, but
            // Closeable interface requires close method to be idempotent:
            // "if the stream is already closed then invoking this method has no effect." (c)
            closeWithRuntimeException(result)
        }
        return result
    }

    fun <T> getTag(key: String): T? {
        synchronized(mBagOfTags) {
            return mBagOfTags[key] as T?
        }
    }

    companion object {

        private fun closeWithRuntimeException(obj: Any) {
            if (obj is Closeable) {
                try {
                    obj.close()
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        }
    }
}