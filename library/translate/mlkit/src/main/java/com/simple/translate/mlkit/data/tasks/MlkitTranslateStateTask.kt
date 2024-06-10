package com.simple.translate.mlkit.data.tasks

import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.simple.state.ResultState
import com.simple.translate.data.tasks.TranslateStateTask
import com.simple.translate.mlkit.utils.exts.checkModelDownloaded

class MlkitTranslateStateTask : TranslateStateTask {

    override suspend fun executeTask(param: TranslateStateTask.Param): Int {

        return if (param.languageCode !in TranslateLanguage.getAllLanguages()) {

            -1
        } else {

            checkDownloadModel(param.languageCode)
        }
    }

    private suspend fun checkDownloadModel(languageCode: String): Int {

        val state = TranslateRemoteModel.Builder(languageCode).build().checkModelDownloaded()

        return if (state is ResultState.Success) {

            if (state.data) 1 else 0
        } else {

            -1
        }
    }
}