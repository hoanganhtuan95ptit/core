package com.simple.detect_2

import android.graphics.Bitmap
import com.simple.autobind.AutoBind
import com.simple.detect_2.entities.Paragraph
import com.simple.state.ResultState

interface DetectTask {

    suspend fun isSupportState(languageCode: String): ResultState<Boolean>

    suspend fun isSupport(languageCode: String): Boolean


    suspend fun detectState(source: Bitmap): ResultState<List<Paragraph>>

    suspend fun detect(source: Bitmap): List<Paragraph>


    companion object {

        val instant = AutoBind.loadAsync(DetectTask::class.java, true)
    }
}