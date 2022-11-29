package com.one.coreapp.utils.extentions

import com.one.coreapp.ui.base.servicer.BaseService
import kotlinx.coroutines.*
import java.io.Closeable
import kotlin.coroutines.CoroutineContext


private const val JOB_KEY = "com.tuanha.coreapp.utils.extentions.ServiceCoroutineScope.JOB_KEY"

@OptIn(ObsoleteCoroutinesApi::class)
val BaseService.serviceScope: CoroutineScope
    get() {
        val scope: CoroutineScope? = this.getTag(JOB_KEY)
        if (scope != null) {
            return scope
        }
        return setTagIfAbsent(
            JOB_KEY,
            CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        )
    }

internal class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }
}
