package com.simple.detect_2

import android.graphics.Bitmap
import com.simple.autobind.AutoBind
import com.simple.detect_2.entities.Paragraph

interface DetectTask {

    fun isSupport(languageCode: String): Boolean

    suspend fun detect(source: Bitmap): List<Paragraph>

    companion object {

        val instant = AutoBind.loadAsync(DetectTask::class.java, true)
    }
}