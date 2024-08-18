package com.simple.translate.mlkit.data.tasks

import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.simple.state.ResultState
import com.simple.translate.data.tasks.TranslateStateTask
import com.simple.translate.entities.TranslateProvider
import com.simple.translate.entities.TranslateState
import com.simple.translate.mlkit.utils.exts.checkModelDownloaded

class MlkitTranslateStateTask : TranslateStateTask {

    override suspend fun executeTask(param: TranslateStateTask.Param): Pair<TranslateProvider, TranslateState> {

        val state = if (param.languageCode !in TranslateLanguage.getAllLanguages()) {

            TranslateState.NO_SUPPORT
        } else {

            checkDownloadModel(param.languageCode)
        }

        return TranslateProvider.OFFLINE to state
    }

    private suspend fun checkDownloadModel(languageCode: String): TranslateState {

        val state = TranslateRemoteModel.Builder(languageCode).build().checkModelDownloaded()

        return if (state is ResultState.Success) {

            if (state.data) TranslateState.READY else TranslateState.NOT_READY
        } else {

            TranslateState.NO_SUPPORT
        }
    }
}