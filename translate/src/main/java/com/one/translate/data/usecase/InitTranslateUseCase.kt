package com.one.translate.data.usecase

import com.one.coreapp.data.usecase.BaseUseCase
import com.one.translate.HandleTranslate
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class InitTranslateUseCase(
    private val list: List<HandleTranslate>
) : BaseUseCase<InitTranslateUseCase.Param, Flow<String?>> {

    override suspend fun execute(param: Param?): Flow<String?> = channelFlow {
        checkNotNull(param)

        list.sortedByDescending {

            it.priority()
        }.map {

            async {

                it.init(param.inputCode, param.outputCode)
            }
        }

        awaitClose {

        }
    }

    data class Param(val word: String, val inputCode: String, val outputCode: String) : BaseUseCase.Param()
}