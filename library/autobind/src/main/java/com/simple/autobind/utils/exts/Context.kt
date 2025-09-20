package com.simple.autobind.utils.exts

import android.content.Context
import com.google.gson.Gson
import com.simple.autobind.entities.Binding


internal fun Context.reloadBinding(): List<Binding> {

    val gson = Gson()

    return assets.reloadBinding(gson)
}