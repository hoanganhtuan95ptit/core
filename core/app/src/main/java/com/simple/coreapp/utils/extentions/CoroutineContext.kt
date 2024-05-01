package com.simple.coreapp.utils.extentions

import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext

suspend fun isActive() = coroutineContext.isActive