package com.unknown.color.provider.def

import androidx.fragment.app.FragmentActivity
import com.google.auto.service.AutoService
import com.unknown.color.provider.ColorProvider
import com.unknown.color.utils.exts.getColorFromAttr
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

@AutoService(ColorProvider::class)
class DefaultColorProvider : ColorProvider {

    override fun priority(): Int {
        return 0
    }

    override suspend fun provide(activity: FragmentActivity): Flow<Map<String, Int>> = channelFlow {

        val map = hashMapOf<String, Int>()

        listOf(
            android.R.attr::class.java,
        ).flatMap {

            it.fields.toList()
        }.forEach {

            if (it.name.startsWith("color", true)) kotlin.runCatching {

                map.put(it.name, activity.getColorFromAttr(it.getInt(null)))
            }
        }

        trySend(map)

        awaitClose {

        }
    }
}