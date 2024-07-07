package com.simple.detect.mlkit

import android.content.Context
import androidx.startup.Initializer
import com.simple.detect.data.tasks.DetectStateTask
import com.simple.detect.data.tasks.DetectTask
import com.simple.detect.mlkit.data.tasks.MlkitDetectStateTask
import com.simple.detect.mlkit.data.tasks.MlkitDetectTask
import com.simple.detect.mlkit.data.tasks.lanugage.LanguageDetectStateTask
import com.simple.detect.mlkit.data.tasks.lanugage.LanguageDetectTask
import com.simple.detect.mlkit.data.tasks.lanugage.china.ChinaDetectStateTask
import com.simple.detect.mlkit.data.tasks.lanugage.china.ChinaDetectTask
import com.simple.detect.mlkit.data.tasks.lanugage.devanagar.DevanagariDetectStateTask
import com.simple.detect.mlkit.data.tasks.lanugage.devanagar.DevanagariDetectTask
import com.simple.detect.mlkit.data.tasks.lanugage.japan.JapaneseDetectStateTask
import com.simple.detect.mlkit.data.tasks.lanugage.japan.JapaneseDetectTask
import com.simple.detect.mlkit.data.tasks.lanugage.korean.KoreanDetectStateTask
import com.simple.detect.mlkit.data.tasks.lanugage.korean.KoreanDetectTask
import com.simple.detect.mlkit.data.tasks.lanugage.latin.LatinDetectStateTask
import com.simple.detect.mlkit.data.tasks.lanugage.latin.LatinDetectTask
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import org.koin.dsl.module

class MlkitDetectInitializer : Initializer<Unit> {

    override fun create(context: Context) {

        loadKoinModules(listOf(module {

            single { LatinDetectTask() } bind LanguageDetectTask::class

            single { ChinaDetectTask() } bind LanguageDetectTask::class

            single { KoreanDetectTask() } bind LanguageDetectTask::class

            single { JapaneseDetectTask() } bind LanguageDetectTask::class

            single { DevanagariDetectTask() } bind LanguageDetectTask::class

            single { MlkitDetectTask(context, getAll()) } bind DetectTask::class


            single { LatinDetectStateTask() } bind LanguageDetectStateTask::class

            single { ChinaDetectStateTask() } bind LanguageDetectStateTask::class

            single { KoreanDetectStateTask() } bind LanguageDetectStateTask::class

            single { JapaneseDetectStateTask() } bind LanguageDetectStateTask::class

            single { DevanagariDetectStateTask() } bind LanguageDetectStateTask::class


            single { MlkitDetectStateTask(getAll()) } bind DetectStateTask::class

        }))

        return
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}