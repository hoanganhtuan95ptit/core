package com.hoanganhtuan95ptit.autobind.utils.exts

import android.content.Context
import com.google.gson.Gson
import com.hoanganhtuan95ptit.autobind.entities.Binding


internal fun Context.reloadBinding(): List<Binding> {

    val gson = Gson()

    return assets.reloadBinding(gson)
}