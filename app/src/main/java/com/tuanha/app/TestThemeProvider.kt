package com.tuanha.app

import androidx.fragment.app.FragmentActivity
import com.simple.autobind.annotation.AutoBind
import com.unknown.theme.provider.ThemeProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

@AutoBind(ThemeProvider::class)
class Test2ThemeProvider : ThemeProvider {

    override suspend fun provide(activity: FragmentActivity): Flow<Map<String, Any>> = channelFlow {

        trySend(mapOf("test" to 1))

        awaitClose()
    }
}