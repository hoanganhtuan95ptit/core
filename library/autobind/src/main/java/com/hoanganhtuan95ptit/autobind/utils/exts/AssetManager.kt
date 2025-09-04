package com.hoanganhtuan95ptit.autobind.utils.exts

import android.content.res.AssetManager
import com.google.gson.Gson
import com.hoanganhtuan95ptit.autobind.entities.Binding
import com.hoanganhtuan95ptit.autobind.entities.BindingsWrapper

internal fun AssetManager.reloadBinding(gson: Gson = Gson()) = runCatching {

    val allBindings = mutableListOf<Binding>()

    val fileNames = list("autobind") ?: emptyArray()

    for (fileName in fileNames) if (fileName.endsWith(".json")) {

        // Mở file JSON từ assets và đọc toàn bộ nội dung
        val json = open("autobind/$fileName")
            .bufferedReader()
            .use { it.readText() }

        // Parse JSON thành đối tượng BindingsWrapper
        val wrapper = gson.fromJson(json, BindingsWrapper::class.java)

        // Thêm tất cả bindings vào danh sách
        allBindings.addAll(wrapper.bindings)
    }

    allBindings
}.getOrElse {

    emptyList()
}