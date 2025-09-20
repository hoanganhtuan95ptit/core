package com.simple.detect_2

import android.graphics.Bitmap
import com.simple.detect_2.entities.Paragraph

interface DetectTask {

    suspend fun detect(source: Bitmap, inputLanguageCode: String): List<Paragraph>
}