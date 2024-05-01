package com.simple.coreapp.utils.extentions.image

import android.content.Context

data class ImageStr(val data: String) : Image() {

    override fun getImage(context: Context): String {

        return data
    }

    override fun buildImage(context: Context): String {

        return data
    }
}