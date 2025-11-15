package com.simple.detect_2.mlkit.tasks

import android.graphics.Bitmap
import androidx.lifecycle.asFlow
import com.google.android.gms.common.moduleinstall.InstallStatusListener
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_CANCELED
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_COMPLETED
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_FAILED
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import com.simple.crashlytics.logCrashlytics
import com.simple.detect_2.DetectTask
import com.simple.detect_2.entities.Paragraph
import com.simple.detect_2.entities.Sentence
import com.simple.detect_2.entities.Word
import com.simple.detect_2.mlkit.MlkitDetect
import com.simple.state.ResultState
import com.simple.state.doFailed
import com.simple.state.flatMapLatestState
import com.simple.state.isSuccess
import com.simple.state.mapToData
import com.simple.state.toSuccess
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

abstract class MlkitDetectTask() : DetectTask {


    protected abstract fun isSupportLanguage(languageCode: String): Boolean

    protected abstract fun textRecognizerOptionsInterface(): TextRecognizerOptionsInterface


    override suspend fun isSupportState(languageCode: String): ResultState<Boolean> = flow {

        val state = if (isSupportLanguage(languageCode = languageCode)) {

            ResultState.Success(true)
        } else {

            ResultState.Failed(RuntimeException("not suppport language"))
        }

        emit(state)
    }.flatMapLatestState {

        checkAndDownloadAsync()
    }.map {

        it.doFailed {

            logCrashlytics("isSupportState", it)
        }

        it
    }.first()

    override suspend fun isSupport(languageCode: String): Boolean = isSupportState(languageCode = languageCode).isSuccess()


    override suspend fun detectState(source: Bitmap): ResultState<List<Paragraph>> = let {

        checkAndDownloadAsync()
    }.flatMapLatestState {

        detectAsync(source = source)
    }.map {

        it.doFailed {

            logCrashlytics("detectState", it)
        }

        it
    }.first()

    override suspend fun detect(source: Bitmap): List<Paragraph> = detectState(source = source).toSuccess()?.data.orEmpty()


    /**
     * kiểm tra có nên download hay không
     */
    private fun checkAndDownloadAsync() = let {

        checkAvailableAsync()
    }.flatMapLatestState {

        if (it) {
            flowOf(ResultState.Success(true))
        } else {
            downloadModelAsync()
        }
    }

    /**
     * kiểm tra có khả dụng không
     */
    private fun checkAvailableAsync() = channelFlow {

        val application = MlkitDetect.applicationFlow.asFlow().first()

        val moduleInstallClient = ModuleInstall.getClient(application)

        moduleInstallClient.areModulesAvailable(TextRecognition.getClient(textRecognizerOptionsInterface())).addOnSuccessListener { response ->

            trySend(ResultState.Success(response.areModulesAvailable()))
        }.addOnFailureListener {

            trySend(ResultState.Failed(it))
        }.addOnCanceledListener {

            trySend(ResultState.Failed(RuntimeException("cancel")))
        }

        awaitClose {
        }
    }

    private fun downloadModelAsync() = channelFlow {

        val application = MlkitDetect.applicationFlow.asFlow().first()

        val moduleInstallClient = ModuleInstall.getClient(application)

        val installStatusListener = InstallStatusListener { p0 ->

            val installState = p0.installState

            when (installState) {
                STATE_COMPLETED -> {
                    trySend(ResultState.Success(true))
                }

                STATE_FAILED -> {
                    trySend(ResultState.Failed(RuntimeException("failed")))
                }

                STATE_CANCELED -> {
                    trySend(ResultState.Failed(RuntimeException("cancel")))
                }
            }
        }

        val request = ModuleInstallRequest.newBuilder()
            .addApi(TextRecognition.getClient(textRecognizerOptionsInterface()))
            .setListener(installStatusListener)
            .build()

        moduleInstallClient.installModules(request).addOnSuccessListener { response ->

            if (response.areModulesAlreadyInstalled()) trySend(ResultState.Success(true))
        }.addOnFailureListener { exception ->

            trySend(ResultState.Failed(exception))
        }.addOnCanceledListener {

            trySend(ResultState.Failed(RuntimeException("cancel")))
        }

        awaitClose {

        }
    }

    /**
     * tách chứ ra khỏi ảnh
     */
    private fun detectAsync(source: Bitmap) = channelFlow {

        TextRecognition.getClient(textRecognizerOptionsInterface()).process(InputImage.fromBitmap(source, 0)).addOnSuccessListener { visionText ->

            val textBlockList = visionText.textBlocks

            if (textBlockList.isEmpty()) {

                trySend(ResultState.Failed(RuntimeException("empty")))
            } else {

                trySend(ResultState.Success(textBlockList))
            }
        }.addOnFailureListener { e ->

            trySend(ResultState.Failed(e))
        }.addOnCanceledListener {

            trySend(ResultState.Failed(RuntimeException("cancel")))
        }

        awaitClose {

        }
    }.mapToData {

        it.wrapTextBlock()
    }

    private fun List<Text.TextBlock>.wrapTextBlock() = map { _paragraph ->

        val paragraph = Paragraph()

        paragraph.sentences = _paragraph.lines.wrapLine()

        paragraph.text = paragraph.sentences.joinToString(separator = "\n") {

            it.text
        }

        paragraph.points = _paragraph.cornerPoints?.toList()

        paragraph
    }

    private fun List<Text.Line>.wrapLine() = map { _sequence ->

        val sequence = Sentence()

        sequence.words = _sequence.elements.wrapElement()

        sequence.text = sequence.words.joinToString(separator = " ") {

            it.text
        }

        sequence.angle = _sequence.angle
        sequence.points = _sequence.cornerPoints?.toList()
        sequence.confidence = _sequence.confidence

        sequence
    }

    private fun List<Text.Element>.wrapElement() = map { _word ->

        val word = Word()

        word.text = _word.symbols.joinToString(separator = "") {

            it.text
        }

        word.angle = _word.angle
        word.points = _word.cornerPoints?.toList()
        word.confidence = _word.confidence

        word
    }

//
//    private suspend fun identifyLanguage(text: String) = channelFlow {
//
//        val options = LanguageIdentificationOptions.Builder().setConfidenceThreshold(0.34f).build()
//
//        LanguageIdentification.getClient(options).identifyLanguage(text).addOnSuccessListener { languageCode ->
//
//            trySend(languageCode.lowercase())
//        }.addOnFailureListener {
//
//            trySend("")
//        }
//
//        awaitClose {
//
//        }
//    }.first()
//
//    private fun String.wrap(): String {
//        return this
//    }
}