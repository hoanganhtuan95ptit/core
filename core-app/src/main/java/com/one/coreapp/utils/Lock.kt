package com.one.coreapp.utils

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object Lock {

    private val mutex = Mutex()

    private val ownerAndMutex = hashMapOf<Any, Mutex>()

    suspend fun <T> withLock(owner: Any = this, action: suspend () -> T): T {

        var mutex: Mutex

        this.mutex.withLock {

            mutex = ownerAndMutex.get(owner) ?: Mutex().also { mu -> ownerAndMutex[owner] = mu }
        }

        return mutex.withLock {

            action.invoke()
        }
    }
}