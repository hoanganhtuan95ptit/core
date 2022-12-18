package com.one.translate.data.usecase

import com.one.coreapp.data.usecase.BaseUseCase
import com.one.coreapp.utils.extentions.offerActive
import com.one.translate.HandleTranslate
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

class TranslateUseCase(
    private val list: List<HandleTranslate>
) : BaseUseCase<TranslateUseCase.Param, Flow<String?>> {

    override suspend fun execute(param: Param?): Flow<String?> = channelFlow {
        checkNotNull(param)

        val listTranslateAsync = list.sortedByDescending {

            it.priority()
        }.map {

            async {

                val output = it.handle(param.text, param.inputCode, param.outputCode)

                if (output != null) offerActive(output)

                output
            }
        }

        launch {

            val listTranslate = listTranslateAsync.awaitAll()

            if (listTranslate.all { it == null }) offerActive(null)
        }

        awaitClose {

        }
    }

    data class Param(val text: String, val inputCode: String, val outputCode: String) : BaseUseCase.Param()
}